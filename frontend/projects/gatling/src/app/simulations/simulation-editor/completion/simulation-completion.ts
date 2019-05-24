import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class SimulationCompletion extends Completion {

  private static readonly SET_UP_SCORE = 1080;
  private static readonly SET_UP_META = 'Simulation';

  private static readonly INJECT_SCORE = 1179;
  private static readonly INJECT_META = 'Simulation';

  private static readonly CONFIG_SCORE = 1178;
  private static readonly CONFIG_META = 'Simulation Configuration';

  private static readonly PAUSE_SCORE = 1177;
  private static readonly PAUSE_META = 'Simulation Configuration';

  private static readonly INJECT_OPEN_SCORE = 1176;
  private static readonly INJECT_OPEN_META = 'Simulation Injection';

  private static readonly INJECT_CLOSED_SCORE = 1175;
  private static readonly INJECT_CLOSED_META = 'Simulation Injection';

  results(context: CompletionContext): CompletionCommandResult[] {
    return _.concat(
      Completion.matching([
          CompletionCommandResult.fromCommand('setUp()', SimulationCompletion.SET_UP_SCORE, SimulationCompletion.SET_UP_META)
        ],
        context,
        Completion.noDot,
        Completion.noParent),
      Completion.matching([
          CompletionCommandResult.fromCommand('inject()', SimulationCompletion.INJECT_SCORE, SimulationCompletion.INJECT_META)
        ],
        context,
        Completion.dot,
        Completion.parent('setUp')),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('protocols(protocolRef)', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META),
          CompletionCommandResult.fromCommandWithParams('pauses(pauseType)', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META),
          CompletionCommandResult.fromCommandWithParams('uniformPauses(time unit)', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META),
          CompletionCommandResult.fromCommand('customPauses()', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META),
          CompletionCommandResult.fromCommandWithParams('maxDuration(maxDuration)', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META),
          CompletionCommandResult.fromCommandWithParams('throttle(reachRps(target) in (time unit))', SimulationCompletion.CONFIG_SCORE, SimulationCompletion.CONFIG_META)
        ],
        context,
        Completion.dot,
        Completion.noParent,
        Completion.after('setUp')),
      Completion.matching([
          CompletionCommandResult.fromText('disablePauses', SimulationCompletion.PAUSE_SCORE, SimulationCompletion.PAUSE_META),
          CompletionCommandResult.fromText('constantPauses', SimulationCompletion.PAUSE_SCORE, SimulationCompletion.PAUSE_META),
          CompletionCommandResult.fromText('exponentialPauses', SimulationCompletion.PAUSE_SCORE, SimulationCompletion.PAUSE_META),
        ],
        context,
        Completion.noDot,
        Completion.parent('pauses'),
        Completion.afterDirect('pauses')),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('atOnceUsers(nbUsers)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          CompletionCommandResult.fromCommandWithParams('rampUsers(nbUsers) during(dur unit)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          CompletionCommandResult.fromCommandWithParams('constantUsersPerSec(nbUsers) during(dur unit)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          CompletionCommandResult.fromCommandWithParams('rampUsersPerSec(rate1) to (rate2) during(dur unit)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          CompletionCommandResult.fromCommandWithParams('heavisideUsers(nbUsers) during(dur unit)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          CompletionCommandResult.fromCommandWithParams('nothingFor(dur unit)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
          new CompletionCommandResult(
            'incrementUsersPerSec',
            'incrementUsersPerSec',
            'incrementUsersPerSec(incrementUsersPerSec)\n' +
            '  .times(numberOfSteps)\n' +
            '  .eachLevelLasting(levelDuration)\n' +
            '  .separatedByRampsLasting(rampDuration)\n' +
            '  .startingFrom(initialUsersPerSec)', SimulationCompletion.INJECT_OPEN_SCORE, SimulationCompletion.INJECT_OPEN_META),
        ],
        context,
        Completion.noDot,
        Completion.parent('inject'),
        Completion.afterDirect('inject')),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('constantConcurrentUsers(nbUsers) during(duration)',
            SimulationCompletion.INJECT_CLOSED_SCORE, SimulationCompletion.INJECT_CLOSED_META),
          CompletionCommandResult.fromCommandWithParams('rampConcurrentUsers(fromNbUsers) to(toNbUsers) during(duration)',
            SimulationCompletion.INJECT_CLOSED_SCORE, SimulationCompletion.INJECT_CLOSED_META),
          new CompletionCommandResult(
            'incrementConcurrentUsers',
            'incrementConcurrentUsers',
            'incrementConcurrentUsers(incrementConcurrentUsers)\n' +
            '  .times(numberOfSteps)\n' +
            '  .eachLevelLasting(levelDuration)\n' +
            '  .separatedByRampsLasting(rampDuration)\n' +
            '  .startingFrom(initialConcurrentUsers)', SimulationCompletion.INJECT_CLOSED_SCORE, SimulationCompletion.INJECT_CLOSED_META)
        ],
        context,
        Completion.noDot,
        Completion.parent('inject'),
        Completion.afterDirect('inject'))
    );
  }

}


