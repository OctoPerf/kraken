import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import * as _ from 'lodash';

export abstract class Completion {

  static noParent(context: CompletionContext): boolean {
    return !context.parentKeyword;
  }

  static noDot(context: CompletionContext): boolean {
    return !context.endsWithDot;
  }

  static dot(context: CompletionContext): boolean {
    return context.endsWithDot;
  }

  static parent(...keywords: string[]): (context: CompletionContext) => boolean {
    return context => _.reduce(keywords, (acc, keyword) => acc || keyword === context.parentKeyword, false);
  }

  static afterDirect(...keywords: string[]): (context: CompletionContext) => boolean {
    return Completion.afterDirectArray(keywords);
  }

  static afterDirectArray(keywords: string[]): (context: CompletionContext) => boolean {
    return context => _.reduce(keywords, (acc, keyword) => acc || keyword === context.lastKeyword, false);
  }

  static afterWithParent(keyword: string): (context: CompletionContext) => boolean {
    return context => _.lastIndexOf(context.precedingKeywords, keyword) > context.parentIndex;
  }

  static after(...keywords: string[]): (context: CompletionContext) => boolean {
    return Completion.afterArray(keywords);
  }

  static afterArray(keywords: string[]): (context: CompletionContext) => boolean {
    return context => _.intersection(context.precedingKeywords, keywords).length > 0;
  }

  static matching(results: CompletionCommandResult[],
                  context: CompletionContext,
                  ...matchers: Array<(context: CompletionContext) => boolean>): CompletionCommandResult[] {
    for (let i = 0; i < matchers.length; i++) {
      if (!matchers[i](context)) {
        return [];
      }
    }
    return results;
  }

  abstract results(context: CompletionContext): CompletionCommandResult[];

}
