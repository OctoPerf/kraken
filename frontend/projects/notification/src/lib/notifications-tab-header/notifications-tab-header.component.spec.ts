import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {Component} from '@angular/core';
import {NotificationsTabHeaderComponent} from 'projects/notification/src/lib/notifications-tab-header/notifications-tab-header.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NotificationModule} from 'projects/notification/src/lib/notification.module';
import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {SIDE_HEADER_DATA, TAB_HEADER_DATA} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('NotificationsTabHeaderComponent', () => {
  let component: NotificationsTabHeaderComponent;
  let fixture: ComponentFixture<NotificationsTabHeaderComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, NotificationModule],
      declarations: [TestComponent],
      providers: [
        NotificationService,
        TabsService,
        {provide: TAB_HEADER_DATA, useValue: newTestTab(TestComponent)},
        {provide: SIDE_HEADER_DATA, useValue: TabsSide.TOP},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsTabHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
