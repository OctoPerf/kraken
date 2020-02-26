import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {TabsModule} from 'projects/tabs/src/lib/tabs.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {NotificationsTabHeaderComponent} from 'projects/notification/src/lib/notifications-tab-header/notifications-tab-header.component';
import {NotificationsTableComponent} from 'projects/notification/src/lib/notifications-table/notifications-table.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {NotificationLevelToIconPipe} from './notification-level-to-icon.pipe';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EventModule,
    TabsModule,
    HelpModule,
    IconModule,
  ],
  declarations: [
    NotificationsTabHeaderComponent,
    NotificationsTableComponent,
    NotificationLevelToIconPipe,
  ],
  exports: [
    NotificationsTabHeaderComponent,
    NotificationsTableComponent
  ],
})
export class NotificationModule {
}
