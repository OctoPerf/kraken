import {VariablesAutoCompleter} from 'projects/editor/src/lib/variables-auto-completer';
import {CompletionCallback} from 'projects/editor/src/lib/completion-callback';
import {Ace} from 'ace-builds';
import {Injectable} from '@angular/core';
import {StringToolsService} from 'projects/tools/src/lib/string-tools.service';
import * as _ from 'lodash';
import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import {ScenarioCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/scenario-completion';
import {HttpCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/http-completion';
import {SimulationCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/simulation-completion';
import {FeederCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/feeder-completion';
import {CheckCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/check-completion';
import {AssertionCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/assertion-completion';
import {HttpProtocolCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/http-protocol-completion';

@Injectable()
export class GatlingVariablesAutoCompleter implements VariablesAutoCompleter {

  private static readonly SEARCH_LINES_COUNT = 300;
  private static readonly LINE_COMMENT_REGEXP = /\/\/.*\n/gm;
  private static readonly MULTILINE_COMMENT_REGEXP = /\/\*[\s\S]*?\*\//gm;
  private static readonly QUOTED_TEXT_REGEXP = /".*?"/gm;
  private static readonly CLEAN_REGEXPS = [
    GatlingVariablesAutoCompleter.LINE_COMMENT_REGEXP,
    GatlingVariablesAutoCompleter.MULTILINE_COMMENT_REGEXP,
    GatlingVariablesAutoCompleter.QUOTED_TEXT_REGEXP,
  ];
  private static readonly KEYWORD_REGEXP = /(?:(\w+)\(|\.(\w+)|(\w+)\.|(\w+)\n)/;
  private static readonly LAST_WORD_REGEXP = /(\w+)$/;

  private static COMPLETIONS: Completion[] = [
    new ScenarioCompletion(),
    new HttpCompletion(),
    new CheckCompletion(),
    new SimulationCompletion(),
    new FeederCompletion(),
    new AssertionCompletion(),
    new HttpProtocolCompletion(),
  ];

  constructor(private stringTools: StringToolsService) {
  }

  autoCompleteVariableNames(editor: Ace.Editor, session: any, pos: Ace.Point, prefix: string, callback: CompletionCallback) {
    const context = this._getCompletionContext(editor, pos, prefix);
    const results = _.chain(GatlingVariablesAutoCompleter.COMPLETIONS)
      .map(completion => completion.results(context))
      .flatten()
      .map((result: any) => {
        result.completer = {
          insertMatch: this._insertMatch.bind(this)
        };
        return result;
      })
      .value();
    callback(null, results as any);
  }

  _insertMatch(editor: Ace.Editor, data: CompletionCommandResult) {
    let pos: Ace.Point = editor.getCursorPosition();
    const currentLine = editor.getSession().getLine(pos.row).slice(0, pos.column);
    const lastWord = GatlingVariablesAutoCompleter.LAST_WORD_REGEXP.exec(currentLine);
    if (lastWord) {
      // Remove last word before injecting the auto-complete value
      editor.session.remove({start: {row: pos.row, column: pos.column - lastWord[1].length}, end: pos} as any);
    }
    pos = editor.getCursorPosition();
    editor.session.insert(pos, data.command);

    pos = editor.getCursorPosition();
    // Got the previous row (to put caret between parentheses)
    const start: Ace.Point = {row: pos.row, column: pos.column - data.gotoOffset};
    editor.gotoLine(start.row + 1, start.column, false);

    // And select text (i.e. "name" for scenario("name"))
    const end: Ace.Point = {row: start.row, column: start.column + data.selectionLength};
    editor.selection.setRange({start, end} as any);

    // TODO Update documentation url
  }

  _getCompletionContext(editor: Ace.Editor, pos: Ace.Point, prefix: string): CompletionContext {
    const text = this._getPrecedingText(editor, pos);
    const clean = this._cleanText(text);
    const keywords = this._getPrecedingKeywordExecs(clean);
    const parentIndex = this._getParentKeywordIndex(clean, keywords);
    const precedingKeywords = _.map(keywords, (keyword: RegExpExecArray) => _.findLast(keyword, val => !!val));
    return {
      precedingKeywords,
      lastKeyword: _.last(precedingKeywords),
      parentKeyword: parentIndex === -1 ? null : precedingKeywords[parentIndex],
      parentIndex,
      endsWithDot: _.last(text.slice(0, text.length - prefix.length)) === '.'
    };
  }

  _getPrecedingText(editor: Ace.Editor, pos: Ace.Point): string {
    const range: any = {
      start: {row: Math.max(0, pos.row - GatlingVariablesAutoCompleter.SEARCH_LINES_COUNT), col: 0},
      end: pos,
    };
    return editor.session.getTextRange(range);
  }

  _cleanText(text: string): string {
    _.forEach(GatlingVariablesAutoCompleter.CLEAN_REGEXPS, (regexp: RegExp) => {
      text = this.stringTools.replaceAll(text, regexp, '');
    });

    // Remove all text preceding code separators
    const lastIndex = _.max([text.lastIndexOf('{'), text.lastIndexOf('}'), text.lastIndexOf('val '), text.lastIndexOf('setUp(')]);
    if (lastIndex > -1) {
      text = text.substring(lastIndex, text.length + 1);
    }

    return text;
  }

  _getPrecedingKeywordExecs(text: string): RegExpExecArray[] {
    const result: RegExpExecArray[] = [];
    const regexp = new RegExp(GatlingVariablesAutoCompleter.KEYWORD_REGEXP, 'gm');
    let match = regexp.exec(text);
    while (match != null) {
      result.push(match);
      match = regexp.exec(text);
    }
    return result;
  }

  _getParentKeywordIndex(text: string, keywords: RegExpExecArray[]): number {
    if (!keywords.length) {
      return -1;
    }
    let count = 0;
    let index = text.length - 1;
    while (count >= 0 && index >= 0) {
      const char = text[index];
      if (char === ')') {
        count++;
      } else if (char === '(') {
        count--;
      }
      index--;
    }
    if (index === -1) {
      return -1;
    }
    return _.findLastIndex(keywords, (array: RegExpExecArray) => array.index < index);
  }
}
