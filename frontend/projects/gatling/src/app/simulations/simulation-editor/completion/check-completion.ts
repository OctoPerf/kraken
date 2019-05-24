import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';
import {HttpCompletion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/http-completion';

export class CheckCompletion extends Completion {

  private static readonly CHECK_SCORE = 1030;
  private static readonly CHECK_META = 'Check';

  private static readonly CHECK_PARAM_SCORE = 1060;

  private static readonly HTTP_SCORE = 1029;
  private static readonly HTTP_META = 'HTTP Check';

  private static readonly VERIFIER_SCORE = 1028;
  private static readonly VERIFIER_META = 'Check Verifier';

  private static readonly SAVE_SCORE = 1027;
  private static readonly SAVE_META = 'Check Save';

  results(context: CompletionContext): CompletionCommandResult[] {

    const http = [
      CompletionCommandResult.fromText('status', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('currentLocation', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('responseTimeInMillis', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('bodyString', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('bodyBytes', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('md5', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromText('sha1', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('header(headerName)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('headerRegex(headerName, pattern)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('substring(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('regex(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('xpath(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('jsonPath(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('jsonpJsonPath(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('css(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
      CompletionCommandResult.fromCommandWithParams('form(expression)', CheckCompletion.HTTP_SCORE, CheckCompletion.HTTP_META),
    ];

    const verifier = [
      CompletionCommandResult.fromCommandWithParams('find(occurrence)', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('findAll', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('findRandom', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('count', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromCommandWithParams('is(expected)', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromCommandWithParams('not(expected)', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('exists', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('notExists', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromCommandWithParams('in(sequence)', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('optional', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('isNull', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
      CompletionCommandResult.fromText('notNull', CheckCompletion.VERIFIER_SCORE, CheckCompletion.VERIFIER_META),
    ];

    return _.concat(
      Completion.matching([CompletionCommandResult.fromCommandWithParams('check(check...)', CheckCompletion.CHECK_SCORE, CheckCompletion.CHECK_META)],
        context,
        Completion.dot,
        Completion.parent('exec', 'resources'),
        Completion.afterWithParent('http'),
        HttpCompletion.afterVerb,
      ),
      Completion.matching([
          CompletionCommandResult.fromText('ignoreDefaultChecks', CheckCompletion.CHECK_PARAM_SCORE, CheckCompletion.CHECK_META),
        ],
        context,
        Completion.dot,
        Completion.afterWithParent('check'),
      ),
      Completion.matching(http,
        context,
        Completion.noDot,
        Completion.parent('check'),
      ),
      Completion.matching(verifier,
        context,
        Completion.dot,
        Completion.parent('check'),
        Completion.afterDirectArray(_.map(http, 'name'))
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('name(key)', CheckCompletion.SAVE_SCORE, CheckCompletion.SAVE_META),
          CompletionCommandResult.fromCommandWithParams('saveAs(key)', CheckCompletion.SAVE_SCORE, CheckCompletion.SAVE_META),
        ],
        context,
        Completion.dot,
        Completion.parent('check'),
        Completion.afterDirectArray(_.map(verifier, 'name'))
      ),
    );
  }
}
