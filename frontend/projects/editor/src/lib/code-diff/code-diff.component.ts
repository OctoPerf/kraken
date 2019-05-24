import {AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, Input, OnInit} from '@angular/core';
import * as CodeMirror from 'codemirror';
import 'codemirror/addon/merge/merge';
import 'codemirror/mode/xml/xml.js';
import 'codemirror/mode/http/http.js';
import 'codemirror/mode/htmlmixed/htmlmixed.js';
import 'codemirror/mode/css/css.js';
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/addon/scroll/annotatescrollbar.js';
import 'codemirror/addon/search/matchesonscrollbar.js';
import 'codemirror/addon/search/search.js';
import 'codemirror/addon/search/searchcursor.js';
import 'codemirror/addon/dialog/dialog.js';
import {DiffMode} from 'projects/editor/src/lib/code-diff/diff-mode';
import MergeViewEditor = CodeMirror.MergeView.MergeViewEditor;

declare var require: any;

// declare global: diff_match_patch, DIFF_INSERT, DIFF_DELETE, DIFF_EQUAL
(window as any).diff_match_patch = require('diff-match-patch');
(window as any).DIFF_DELETE = -1;
(window as any).DIFF_INSERT = 1;
(window as any).DIFF_EQUAL = 0;

const codeMirror: any = (window as any).CodeMirror = require('codemirror');

@Component({
  selector: 'lib-code-diff',
  template: '',
  styleUrls: ['./code-diff.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CodeDiffComponent implements OnInit, AfterViewInit {

  @Input() left: string;
  @Input() right: string;
  @Input() mode: DiffMode = 'http';
  mergeView: MergeViewEditor;

  constructor(public elRef: ElementRef) {
  }

  ngOnInit() {
    this.elRef.nativeElement.innerHTML = '';
    this.mergeView = codeMirror.MergeView(this.elRef.nativeElement, {
      readOnly: true,
      revertButtons: false,
      value: this.left,
      orig: this.right,
      lineNumbers: true,
      mode: this.mode,
      theme: 'darcula',
      collapseIdentical: true,
    });
  }

  ngAfterViewInit() {
    this.resize();
  }

  resize() {
    // Mandatory to avoid refresh issues (either the content is not displayed or the line numbers are messed up).
    if (this.mergeView.rightOriginal().refresh) {
      this.mergeView.rightOriginal().refresh();
    }
    if (this.mergeView.editor().refresh) {
      this.mergeView.editor().refresh();
    }
  }
}
