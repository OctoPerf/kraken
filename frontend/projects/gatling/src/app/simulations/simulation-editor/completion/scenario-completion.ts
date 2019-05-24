import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class ScenarioCompletion extends Completion {

  private static readonly SCENARIO_SCORE = 1100;
  private static readonly SCENARIO_META = 'Scenario';

  private static readonly BASE_SCORE = 1099;
  private static readonly BASE_META = 'Base Structure';

  private static readonly LOOP_SCORE = 1098;
  private static readonly LOOP_META = 'Loop';

  private static readonly CONDITION_SCORE = 1097;
  private static readonly CONDITION_META = 'Condition';

  private static readonly ERROR_SCORE = 1096;
  private static readonly ERROR_META = 'Errors Handling';

  results(context: CompletionContext): CompletionCommandResult[] {
    return _.concat(
      Completion.matching([
        CompletionCommandResult.fromCommandWithParams('scenario(name)', ScenarioCompletion.SCENARIO_SCORE, ScenarioCompletion.SCENARIO_META)
        ],
      context, Completion.noParent, Completion.noDot),
      Completion.matching([
          CompletionCommandResult.fromCommand('exec()', ScenarioCompletion.BASE_SCORE, ScenarioCompletion.BASE_META),
          CompletionCommandResult.fromCommandWithParams('group(name){chain}', ScenarioCompletion.BASE_SCORE, ScenarioCompletion.BASE_META),
          CompletionCommandResult.fromCommandWithParams('pause(time unit)', ScenarioCompletion.BASE_SCORE, ScenarioCompletion.BASE_META),
          CompletionCommandResult.fromCommandWithParams('pace(time unit)', ScenarioCompletion.BASE_SCORE, ScenarioCompletion.BASE_META),
          CompletionCommandResult.fromCommandWithParams('rendezVous(userNumber)', ScenarioCompletion.BASE_SCORE, ScenarioCompletion.BASE_META),
        ],
        context,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('repeat(times){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('during(10 seconds){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('asLongAs(condition){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('foreach(sequence, elementName){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('doWhile(condition){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('asLongAsDuring(condition, duration, counterName){chain}',            ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('doWhileDuring(condition, duration, counterName){chain}',            ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
          CompletionCommandResult.fromCommandWithParams('forever(counterName){chain}', ScenarioCompletion.LOOP_SCORE, ScenarioCompletion.LOOP_META),
        ],
        context,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('doIf(condition){chain}', ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('doIfEquals(expected, actual){chain}',      ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('doIfOrElse(condition){chain}{otherChain}',      ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('doIfEqualsOrElse(expected, actual){chain}{otherChain}',      ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('doSwitch(key){switch}', ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('doSwitchOrElse(key){switch}{chain}',      ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('randomSwitch(Map[%,Chain])', ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('randomSwitchOrElse(Map[%,Chain]){chain}',      ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('uniformRandomSwitch(chains...)', ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
          CompletionCommandResult.fromCommandWithParams('roundRobinSwitch(chains...)', ScenarioCompletion.CONDITION_SCORE, ScenarioCompletion.CONDITION_META),
        ],
        context,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('tryMax(times){chain}', ScenarioCompletion.ERROR_SCORE, ScenarioCompletion.ERROR_META),
          CompletionCommandResult.fromCommandWithChain('exitBlockOnFail{chain}', ScenarioCompletion.ERROR_SCORE, ScenarioCompletion.ERROR_META),
        ],
        context,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromText('exitHereIfFailed', ScenarioCompletion.ERROR_SCORE, ScenarioCompletion.ERROR_META),
        ],
        context,
        Completion.dot,
        Completion.noParent,
        Completion.after('exec'))
    );
  }

}
