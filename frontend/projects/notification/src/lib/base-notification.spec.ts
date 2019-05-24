import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';

export const testNotification: () => BaseNotification = () => new BaseNotification('test');

describe('BaseNotification', () => {

  it('should be created', () => {
    expect(testNotification()).toBeTruthy();
  });

  it('should be created with all constructor fields', () => {
    expect(new BaseNotification(`TADA !`, NotificationLevel.ERROR, 'HOME', {
      selector: '.test',
      busEvent: new BusEvent('open-left-tab')
    })).toBeTruthy();
  });
});


