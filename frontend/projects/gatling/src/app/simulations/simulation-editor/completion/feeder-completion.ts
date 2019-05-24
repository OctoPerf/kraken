import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class FeederCompletion extends Completion {

  private static readonly TYPE_SCORE = 1030;
  private static readonly TYPE_META = 'Feeder';

  private static readonly OPTION_SCORE = 1129;
  private static readonly OPTION_META = 'Feeder Option';

  private static readonly STRATEGY_SCORE = 1128;
  private static readonly STRATEGY_META = 'Feeder Strategy';

  results(context: CompletionContext): CompletionCommandResult[] {
    return _.concat(
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('csv(fileName)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('tsv(fileName)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('ssv(fileName)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('jsonFile(fileName)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('jsonUrl(url)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('jdbcFeeder(dbURL,user,pass,sql)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
          CompletionCommandResult.fromCommandWithParams('redisFeeder(pool, key)', FeederCompletion.TYPE_SCORE, FeederCompletion.TYPE_META),
        ],
        context,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('batch(bufferSize)', FeederCompletion.OPTION_SCORE, FeederCompletion.OPTION_META),
          CompletionCommandResult.fromText('unzip', FeederCompletion.OPTION_SCORE, FeederCompletion.OPTION_META),
          CompletionCommandResult.fromText('readRecords', FeederCompletion.OPTION_SCORE, FeederCompletion.OPTION_META),
        ],
        context,
        Completion.noParent,
        Completion.dot,
        Completion.after('csv', 'tsv', 'ssv', 'jsonFile', 'jsonUrl', 'jdbcFeeder', 'redisFeeder')),
      Completion.matching([
          CompletionCommandResult.fromText('queue', FeederCompletion.STRATEGY_SCORE, FeederCompletion.STRATEGY_META),
          CompletionCommandResult.fromText('random', FeederCompletion.STRATEGY_SCORE, FeederCompletion.STRATEGY_META),
          CompletionCommandResult.fromText('circular', FeederCompletion.STRATEGY_SCORE, FeederCompletion.STRATEGY_META),
          CompletionCommandResult.fromText('shuffle', FeederCompletion.STRATEGY_SCORE, FeederCompletion.STRATEGY_META),
        ],
        context,
        Completion.noParent,
        Completion.dot,
        Completion.after('csv', 'tsv', 'ssv', 'jsonFile', 'jsonUrl', 'jdbcFeeder', 'redisFeeder')),
    );
  }

}


