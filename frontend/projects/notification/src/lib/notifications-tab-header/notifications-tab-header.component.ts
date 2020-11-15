import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {
  SIDE_HEADER_DATA,
  TAB_HEADER_DATA,
  TabHeaderComponent
} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {IconFaCounter} from 'projects/icon/src/lib/icon-fa-counter';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {Tab} from 'projects/tabs/src/lib/tab';
import {Subscription} from 'rxjs';

@Component({
  selector: 'lib-notifications-tab-header',
  templateUrl: './notifications-tab-header.component.html',
  styleUrls: ['./notifications-tab-header.component.scss']
})
export class NotificationsTabHeaderComponent extends TabHeaderComponent implements OnInit, OnDestroy {

  public icon: IconFaCounter;
  private notificationSubscription: Subscription;

  constructor(@Inject(TAB_HEADER_DATA) tab: Tab,
              @Inject(SIDE_HEADER_DATA) side: TabsSide,
              tabsService: TabsService,
              eventBus: EventBusService,
              public notificationsService: NotificationService) {
    super(tab, side, tabsService, eventBus);
  }

  ngOnInit() {
    this.icon = new IconFaCounter(this.tab.icon as IconFa, '', 'error');
    this.notificationSubscription = this.notificationsService.notificationsSubject.subscribe(() => {
      this.icon.content = `${Math.min(this.notificationsService.count, 99)}`;
    });
  }

  ngOnDestroy() {
    super.ngOnDestroy();
    this.notificationSubscription.unsubscribe();
  }

}
