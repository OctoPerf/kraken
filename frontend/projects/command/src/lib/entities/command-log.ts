import {Command} from 'projects/command/src/lib/entities/command';
import {CommandLogStatus} from 'projects/command/src/lib/entities/command-log-status';

export interface CommandLog {
  command: Command;
  text?: string;
  status: CommandLogStatus;
}
