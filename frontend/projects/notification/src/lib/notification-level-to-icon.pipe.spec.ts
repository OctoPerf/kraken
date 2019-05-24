import {NotificationLevelToIconPipe} from './notification-level-to-icon.pipe';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';

describe('NotificationLevelToIconPipe', () => {
  it('create an instance', () => {
    const pipe = new NotificationLevelToIconPipe();
    expect(pipe).toBeTruthy();
  });

  it('create return icon', () => {
    const pipe = new NotificationLevelToIconPipe();
    expect(pipe.transform(NotificationLevel.INFO)).toBeTruthy();
    expect(pipe.transform(NotificationLevel.WARNING)).toBeTruthy();
    expect(pipe.transform(NotificationLevel.ERROR)).toBeTruthy();
  });
});
