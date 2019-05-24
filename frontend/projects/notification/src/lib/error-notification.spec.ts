import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {ErrorNotification} from 'projects/notification/src/lib/error-notification';

export const testErrorNotification: () => ErrorNotification = () => new ErrorNotification('test');

describe('ErrorNotification', () => {

  it('should be created', () => {
    expect(testErrorNotification()).toBeTruthy();
  });

  it('should be created with all constructor fields', () => {
    expect(new ErrorNotification(`TADA !`, NotificationLevel.WARNING, 'trace')).toBeTruthy();
  });
});


