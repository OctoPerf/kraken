import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PrettyStringPipe} from './pretty-string.pipe';
import { FileSizePipe } from './file-size.pipe';
import { MarkdownToHtmlPipe } from './markdown-to-html.pipe';
import { QueryParamsToStringPipe } from './query-params-to-string.pipe';
import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';
import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';

@NgModule({
  imports: [
    CommonModule,
  ],
  declarations: [PrettyStringPipe, FileSizePipe, MarkdownToHtmlPipe, QueryParamsToStringPipe, PathToNamePipe, PathToParentPathPipe],
  exports: [PrettyStringPipe, FileSizePipe, MarkdownToHtmlPipe, QueryParamsToStringPipe, PathToNamePipe, PathToParentPathPipe],
  providers: [QueryParamsToStringPipe, PathToNamePipe, PathToParentPathPipe]
})
export class ToolsModule {
}
