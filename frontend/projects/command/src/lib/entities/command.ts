export class Command {

  public static readonly SHELL_0 = '/bin/sh';
  public static readonly SHELL_1 = '-c';

  constructor(public readonly command: string[],
              public readonly environment: { [key in string]: string } = {},
              public path = '',
              public onCancel: string[] = [],
              public applicationId = '',
              public readonly id = '') {
  }

}
