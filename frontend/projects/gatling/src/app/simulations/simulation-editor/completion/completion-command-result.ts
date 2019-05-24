export class CompletionCommandResult {

  private static readonly COMMAND_REGEXP = /(\w+)\(/;
  private static readonly COMMAND_WITH_CHAIN_REGEXP = /(\w+)(\(.*\))?\{(.*)\}/;
  private static readonly COMMAND_WITH_PARAMS_REGEXP = /(\w+)\((.*)\)/;

  public static fromText(text: string, score: number, meta: string): CompletionCommandResult {
    return new CompletionCommandResult(
      text,
      text,
      text,
      score,
      meta,
    );
  }

  public static fromCommand(command: string, score: number, meta: string): CompletionCommandResult {
    const result = CompletionCommandResult.COMMAND_REGEXP.exec(command);
    const name = result[1];
    return new CompletionCommandResult(
      name,
      name,
      command,
      score,
      meta,
      1,
      0
    );
  }

  public static fromCommandWithChain(command: string, score: number, meta: string): CompletionCommandResult {
    return CompletionCommandResult._fromCommandWithParamRegexp(command, score, meta, CompletionCommandResult.COMMAND_WITH_CHAIN_REGEXP);
  }

  public static fromCommandWithParams(command: string, score: number, meta: string): CompletionCommandResult {
    return CompletionCommandResult._fromCommandWithParamRegexp(command, score, meta, CompletionCommandResult.COMMAND_WITH_PARAMS_REGEXP);
  }

  private static _fromCommandWithParamRegexp(command: string, score: number, meta: string, regexp: RegExp): CompletionCommandResult {
    const result = regexp.exec(command);
    const name = result[1];
    const param = result[result.length - 1];
    const paramIndex = command.indexOf(param);
    return new CompletionCommandResult(
      name,
      name,
      command,
      score,
      meta,
      command.length - paramIndex,
      param.length
    );
  }

  constructor(
    public readonly name: string,
    public readonly value: string,
    public readonly command: string,
    public readonly score: number,
    public readonly meta: string,
    public readonly gotoOffset = 0,
    public readonly selectionLength = 0,
  ) {
  }
}
