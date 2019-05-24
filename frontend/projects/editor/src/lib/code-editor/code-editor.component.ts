import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  forwardRef,
  Input,
  OnDestroy,
  OnInit,
  Output
} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {Ace, edit} from 'ace-builds';
import {CodeService} from 'projects/editor/src/lib/code.service';
import {CodeMode} from 'projects/editor/src/lib/code-editor/code-mode';
import {Subscription} from 'rxjs';
import * as _ from 'lodash';
import {VariablesAutoCompleter} from 'projects/editor/src/lib/variables-auto-completer';

@Component({
  selector: 'lib-code-editor',
  template: '',
  styleUrls: ['./code-editor.component.scss'],
  providers: [
    CodeService,
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CodeEditorComponent),
      multi: true
    },
  ],
})
export class CodeEditorComponent implements OnInit, OnDestroy, AfterViewInit, ControlValueAccessor {

  @Input() readonly = false;
  @Input() enableBasicAutoCompletion = true;
  @Input() enableSnippets = false;
  @Input() variablesAutoCompleter?: VariablesAutoCompleter;
  @Input() tabsSize?: number;
  @Output() contentChange = new EventEmitter<string>();
  _value = '';
  _mode: CodeMode = 'text';
  _editor: Ace.Editor;
  _onTouched = new EventEmitter<string>();
  _onChange = new EventEmitter<string>();
  _silent = false;

  private subscriptions: Subscription[] = [];

  constructor(private codeService: CodeService,
              private elRef: ElementRef,
              private changeDetector: ChangeDetectorRef) {
  }

  ngOnInit() {
    this._editor = edit(this.elRef.nativeElement, {
      fontSize: '11pt',
      theme: 'ace/theme/kraken',
      mode: this.sessionMode,
      readOnly: this.readonly,
      value: this._value,
      enableSnippets: this.enableSnippets,
      enableBasicAutocompletion: this.enableBasicAutoCompletion,
    } as any);
  }

  ngAfterViewInit() {
    this.codeService.initCodeEditorComponent(this._editor, this.variablesAutoCompleter);
    this._editor.on('change', () => {
      if (this._silent) {
        return;
      }
      this._value = this.value;
      this._onTouched.emit();
      this._onChange.emit(this._value);
      this.contentChange.emit(this._value);
    });
    if (this.tabsSize) {
      this._editor.getSession().setTabSize(this.tabsSize);
    }
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  registerOnChange(fn: any): void {
    this.subscriptions.push(this._onChange.subscribe(fn));
  }

  registerOnTouched(fn: any): void {
    this.subscriptions.push(this._onTouched.subscribe(fn));
  }

  writeValue(obj: any): void {
    // -1 prevents the _editor from selecting the whole value
    this._editor.setValue(obj, -1);
  }

  setDisabledState(isDisabled: boolean): void {
    this._editor.setReadOnly(isDisabled);
  }

  resize() {
    this._editor.resize(true);
  }

  appendText(text: string) {
    this._value += text;
    const editor = this._editor;
    if (editor) {
      const scrollToBottom = (editor.session.getLength() - editor.renderer.getScrollBottomRow()) <= 2;
      editor.session.insert(
        {
          row: editor.session.getLength(),
          column: 0
        }, text
      );
      if (scrollToBottom) {
        editor.gotoLine(editor.session.getLength(), 0, false);
      }
      // Force refresh of horizontal scrollbar to prevent bug
      const left = editor.session.getScrollLeft();
      editor.session.setScrollLeft(Number.MAX_VALUE);
      editor.session.setScrollLeft(left);

      this.changeDetector.detectChanges();
    }
  }

  @Input() set value(value: string) {
    this._silent = true;
    if (this._editor && value !== this._value) {
      this._editor.getSession().setValue(value);
    }
    this._value = value;
    this._silent = false;
  }

  get value(): string {
    return this._editor.getSession().getValue();
  }

  @Input() set mode(mode: CodeMode) {
    this._mode = mode;
    if (this._editor) {
      this._editor.getSession().setMode(this.sessionMode);
    }
  }

  get sessionMode(): string {
    return `ace/mode/${this._mode}`;
  }
}
