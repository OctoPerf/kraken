import {inject, TestBed} from '@angular/core/testing';
import {NotificationModule} from 'projects/notification/src/lib/notification.module';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';
import {tabsServiceSpy} from 'projects/tabs/src/lib/tabs.service.spec';
import {highlightServiceSpy} from 'projects/help/src/lib/highlight/highlight.service.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {OpenNotificationsEvent} from 'projects/notification/src/lib/open-notifications-event';
import Spy = jasmine.Spy;
import {testNotification} from 'projects/notification/src/lib/base-notification.spec';

describe('NotificationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, NotificationModule],
      providers: [
        NotificationService,
        {provide: TabsService, useValue: tabsServiceSpy()},
        {provide: HighlightService, useValue: highlightServiceSpy()},
      ],
    });
  });

  it('should be created', inject([NotificationService], (service: NotificationService) => {
    expect(service).toBeTruthy();
  }));

  it('should count', inject([NotificationService], (service: NotificationService) => {
    TestBed.inject(EventBusService).publish(new NotificationEvent(testNotification()));
    const count = service.count;
    expect(count).toBe(1);
    expect(service.notifications.length).toBe(1);
    expect(service.notificationsSubject.getValue()).toBe(service.notifications);
  }));

  it('should be destroyed', inject([NotificationService], (service: NotificationService) => {
    service.ngOnDestroy();
    expect().nothing();
  }));

  it('should publish event on snackbar action', inject([NotificationService, EventBusService, TabsService],
    (service: NotificationService, eventBus: EventBusService, tabsService: TabsService) => {
      (tabsService.isSelected as Spy).and.returnValue(false);
      spyOn(eventBus, 'publish');
      service._snackbarAction();
      expect(eventBus.publish).toHaveBeenCalledWith(new OpenNotificationsEvent());
    }));

  it('should publish event on snackbar action', inject([NotificationService, EventBusService, TabsService, HighlightService],
    (service: NotificationService, eventBus: EventBusService, tabsService: TabsService, highlight: HighlightService) => {
      (tabsService.isSelected as Spy).and.returnValue(true);
      spyOn(eventBus, 'publish');
      service._snackbarAction();
      expect(highlight.highlight).toHaveBeenCalledWith('lib-notification-table');
    }));

  it('should clear', inject([NotificationService],
    (service: NotificationService) => {
      service.clear();
      expect(service.notifications).toEqual([]);
    }));
});
