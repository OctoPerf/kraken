import {ContainerStatusIsTerminalPipe} from './container-status-is-terminal.pipe';
import {testTask, testTaskDone} from 'projects/runtime/src/lib/entities/task.spec';

describe('ContainerStatusIsTerminalPipe', () => {
  it('create an instance', () => {
    const pipe = new ContainerStatusIsTerminalPipe();
    expect(pipe).toBeTruthy();
  });
  it('should status terminal', () => {
    const taskDone = testTaskDone();
    const pipe = new ContainerStatusIsTerminalPipe();
    expect(pipe.transform(taskDone.status)).toBe(true);
  });
  it('should status not terminal', () => {
    const task = testTask();
    const pipe = new ContainerStatusIsTerminalPipe();
    expect(pipe.transform(task.status)).toBe(false);
  });
});
