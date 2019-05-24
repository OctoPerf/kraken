import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CodeEditorComponent} from 'projects/editor/src/lib/code-editor/code-editor.component';
import {CodeDiffComponent} from 'projects/editor/src/lib/code-diff/code-diff.component';
import { PathToCodeEditorModePipe } from './path-to-code-editor-mode.pipe';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
  ],
  declarations: [
    CodeEditorComponent,
    CodeDiffComponent,
    PathToCodeEditorModePipe,
  ],
  exports: [
    CodeEditorComponent,
    CodeDiffComponent,
    PathToCodeEditorModePipe,
  ]
})
export class EditorModule {
}
