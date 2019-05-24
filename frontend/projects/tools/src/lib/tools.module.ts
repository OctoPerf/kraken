import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PrettyStringPipe} from './pretty-string.pipe';
import { FileSizePipe } from './file-size.pipe';
import { MarkdownToHtmlPipe } from './markdown-to-html.pipe';
import { QueryParamsToStringPipe } from './query-params-to-string.pipe';

@NgModule({
  imports: [
    CommonModule,
  ],
  declarations: [PrettyStringPipe, FileSizePipe, MarkdownToHtmlPipe, QueryParamsToStringPipe],
  exports: [PrettyStringPipe, FileSizePipe, MarkdownToHtmlPipe, QueryParamsToStringPipe],
  providers: [QueryParamsToStringPipe]
})
export class ToolsModule {
}
