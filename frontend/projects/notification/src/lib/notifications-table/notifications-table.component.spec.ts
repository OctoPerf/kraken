import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {NotificationsTableComponent} from 'projects/notification/src/lib/notifications-table/notifications-table.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NotificationModule} from 'projects/notification/src/lib/notification.module';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {testNotification} from 'projects/notification/src/lib/base-notification.spec';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import SpyObj = jasmine.SpyObj;
import {testErrorNotification} from 'projects/notification/src/lib/error-notification.spec';


describe('NotificationsTableComponent', () => {
  let component: NotificationsTableComponent;
  let fixture: ComponentFixture<NotificationsTableComponent>;
  let dialogs: SpyObj<DialogService>;

  beforeEach(async(() => {
    dialogs = dialogsServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule, NotificationModule],
      providers: [
        NotificationService,
        TabsService,
        {provide: DialogService, useValue: dialogs}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display table', () => {
    TestBed.get(EventBusService).publish(new NotificationEvent(testNotification()));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('td').length).toBe(4);
  });

  it('should datasource disconnect', () => {
    component.dataSource.disconnect(null);
    expect().nothing();
  });

  it('should display error logsComponents', () => {
    component.openTrace(testErrorNotification());
    expect(dialogs.logs).toHaveBeenCalled();
  });

});
