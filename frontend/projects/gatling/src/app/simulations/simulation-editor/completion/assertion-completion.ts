import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class AssertionCompletion extends Completion {

  private static readonly ASSERTION_SCORE = 1200;
  private static readonly ASSERTION_META = 'Assertion';

  private static readonly SCOPE_SCORE = 1199;
  private static readonly SCOPE_META = 'Assertion Scope';

  private static readonly STAT_SCORE = 1198;
  private static readonly STAT_META = 'Assertion Statistic';

  private static readonly COUNT_SCORE = 1197;
  private static readonly COUNT_META = 'Assertion Count';

  private static readonly RESP_TIME_SCORE = 1196;
  private static readonly RESP_TIME_META = 'Assertion Resp. Time';

  private static readonly COMPLETION_SCORE = 1195;
  private static readonly COMPLETION_META = 'Assertion Completion';

  results(context: CompletionContext): CompletionCommandResult[] {
    const scope = [
      CompletionCommandResult.fromText('global', AssertionCompletion.SCOPE_SCORE, AssertionCompletion.SCOPE_META),
      CompletionCommandResult.fromText('forAll', AssertionCompletion.SCOPE_SCORE, AssertionCompletion.SCOPE_META),
      CompletionCommandResult.fromCommandWithParams('details(path)', AssertionCompletion.SCOPE_SCORE, AssertionCompletion.SCOPE_META),
    ];

    const count = [
      CompletionCommandResult.fromText('percent', AssertionCompletion.COUNT_SCORE, AssertionCompletion.COUNT_META),
      CompletionCommandResult.fromText('count', AssertionCompletion.COUNT_SCORE, AssertionCompletion.COUNT_META),
    ];

    const respTime = [
      CompletionCommandResult.fromText('min', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('max', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('mean', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('stdDev', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('percentile1', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('percentile2', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('percentile3', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromText('percentile4', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
      CompletionCommandResult.fromCommandWithParams('percentile(value)', AssertionCompletion.RESP_TIME_SCORE, AssertionCompletion.RESP_TIME_META),
    ];

    return _.concat(
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('assertions(assertions...)', AssertionCompletion.ASSERTION_SCORE, AssertionCompletion.ASSERTION_META),
        ],
        context,
        Completion.dot,
        Completion.after('setUp'),
        Completion.noParent,
      ),
      Completion.matching(scope,
        context,
        Completion.noDot,
        Completion.parent('assertions')),
      Completion.matching([
          CompletionCommandResult.fromText('responseTime', AssertionCompletion.STAT_SCORE, AssertionCompletion.STAT_META),
          CompletionCommandResult.fromText('allRequests', AssertionCompletion.STAT_SCORE, AssertionCompletion.STAT_META),
          CompletionCommandResult.fromText('failedRequests', AssertionCompletion.STAT_SCORE, AssertionCompletion.STAT_META),
          CompletionCommandResult.fromText('successfulRequests', AssertionCompletion.STAT_SCORE, AssertionCompletion.STAT_META),
          CompletionCommandResult.fromText('requestsPerSec', AssertionCompletion.STAT_SCORE, AssertionCompletion.STAT_META),
        ],
        context,
        Completion.dot,
        Completion.parent('assertions'),
        Completion.afterDirectArray(_.map(scope, 'name'))
      ),
      Completion.matching(count,
        context,
        Completion.dot,
        Completion.parent('assertions'),
        Completion.afterDirect('allRequests', 'failedRequests', 'successfulRequests', 'requestsPerSec')
      ),
      Completion.matching(respTime,
        context,
        Completion.dot,
        Completion.parent('assertions'),
        Completion.afterDirect('responseTime', 'failedRequests', 'successfulRequests', 'requestsPerSec')
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('lt(threshold)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('lte(threshold)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('gt(threshold)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('gte(threshold)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('between(thresholdMin,thresholdMax)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('is(value)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
          CompletionCommandResult.fromCommandWithParams('in(sequence)', AssertionCompletion.COMPLETION_SCORE, AssertionCompletion.COMPLETION_META),
        ],
        context,
        Completion.dot,
        Completion.parent('assertions'),
        Completion.afterDirectArray(_.map(_.concat(count, respTime), 'name')),
      ),
    );
  }
}


