import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';

import * as _ from 'lodash';
import {CodeEditorComponent} from 'projects/editor/src/lib/code-editor/code-editor.component';
import {CodeService} from 'projects/editor/src/lib/code.service';
import {codeServiceSpy} from 'projects/editor/src/lib/code.service.spec';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {ChangeDetectorRef, Input} from '@angular/core';
import SpyObj = jasmine.SpyObj;
import {changeDetectorSpy} from 'projects/commons/src/lib/mock/angular.mock.spec';
import {VariablesAutoCompleter} from 'projects/editor/src/lib/variables-auto-completer';
import {KeyBinding} from 'projects/tools/src/lib/key-bindings.service';

describe('CodeEditorComponent', () => {
  let component: CodeEditorComponent;
  let fixture: ComponentFixture<CodeEditorComponent>;
  let codeService: CodeService;
  let changeDetector: SpyObj<ChangeDetectorRef>;

  beforeEach(async(() => {
    codeService = codeServiceSpy();
    changeDetector = changeDetectorSpy();
    TestBed
      .configureTestingModule({
        imports: [EditorModule],
        providers: [
          {provide: CodeService, useValue: codeService},
          {provide: ChangeDetectorRef, useValue: changeDetector}
        ]
      })
      .overrideComponent(CodeEditorComponent, {
        set: {
          providers: [
            {provide: CodeService, useValue: codeService}
          ]
        }
      })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeEditorComponent);
    component = fixture.componentInstance;
    component.variablesAutoCompleter = jasmine.createSpyObj('VariablesAutoCompleter', ['autoCompleteVariableNames']);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init editorComponent', inject([CodeService], (service: CodeService) => {
    component.keyBindings = [
      new KeyBinding(['Del'], ($event: KeyboardEvent) => true)
    ];
    component.ngAfterViewInit();
    expect(service.initCodeEditorComponent).toHaveBeenCalledWith(component._editor, component.variablesAutoCompleter);
  }));

  it('should init editorComponent with tab size', inject([CodeService], (service: CodeService) => {
    component.tabsSize = 42;
    spyOn(component._editor.getSession(), 'setTabSize');
    component.ngAfterViewInit();
    expect(service.initCodeEditorComponent).toHaveBeenCalledWith(component._editor, component.variablesAutoCompleter);
    expect(component._editor.getSession().setTabSize).toHaveBeenCalledWith(42);
  }));

  it('should fire change event', () => {
    spyOn(component.contentChange, 'emit');
    spyOn(component._onChange, 'emit');
    spyOn(component._onTouched, 'emit');
    component.writeValue('test');
    expect(component.contentChange.emit).toHaveBeenCalledWith('test');
    expect(component._onChange.emit).toHaveBeenCalledWith('test');
    expect(component._onTouched.emit).toHaveBeenCalled();
  });

  it('should register change event', () => {
    spyOn(component._onChange, 'subscribe');
    spyOn(component._onTouched, 'subscribe');
    component.registerOnChange(_.noop);
    component.registerOnTouched(_.noop);
    expect(component._onChange.subscribe).toHaveBeenCalled();
    expect(component._onTouched.subscribe).toHaveBeenCalled();
  });

  it('should set disabled state', () => {
    component.setDisabledState(true);
    expect(component._editor.getReadOnly()).toBe(true);
  });

  it('should set mode', () => {
    const spy = spyOn(component._editor.getSession(), 'setMode');
    component.mode = 'java';
    expect(component._mode).toBe('java');
    expect(spy).toHaveBeenCalledWith('ace/mode/java');
  });

  it('should set mode no editor', () => {
    const comp = new CodeEditorComponent(null, null, null);
    comp.mode = 'java';
    expect(comp._mode).toBe('java');
  });

  it('should resize', () => {
    const spy = spyOn(component._editor, 'resize');
    component.resize();
    expect(spy).toHaveBeenCalledWith(true);
  });

  it('should set and get value', () => {
    component.value = 'new value';
    expect(component.value).toBe('new value');
  });

  it('should set value no editor', () => {
    const comp = new CodeEditorComponent(null, null, null);
    comp.value = 'new value';
    expect(comp._value).toBe('new value');
  });

  it('should append text no editor', () => {
    const comp = new CodeEditorComponent(null, null, null);
    comp._value = 'foo';
    comp.appendText('bar');
    expect(comp._value).toBe('foobar');
  });

  it('should append text', () => {
    component.appendText('appended');
  });

});
