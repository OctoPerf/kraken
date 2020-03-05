import { TaskTypeToStringPipe } from './task-type-to-string.pipe';

describe('TaskTypeToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new TaskTypeToStringPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform('GATLING_RUN')).toBe('Run');
  });
});
