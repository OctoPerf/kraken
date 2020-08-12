import {Injectable} from '@angular/core';
import {require} from 'ace-builds';
import * as _ from 'lodash';
import {CodeSnippet} from 'projects/editor/src/lib/code-snippet';
import {CodeMode} from 'projects/editor/src/lib/code-editor/code-mode';

@Injectable()
export class CodeSnippetService {

  load(snippets: CodeSnippet[], mode: CodeMode) {
    const snippetManager = require('ace/snippets').snippetManager;
    snippetManager.register(_.cloneDeep(snippets), mode);
  }

}
