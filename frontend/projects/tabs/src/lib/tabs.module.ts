import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {TabsHeaderComponent} from 'projects/tabs/src/lib/tabs-header/tabs-header.component';
import {TabsContentComponent} from 'projects/tabs/src/lib/tabs-content/tabs-content.component';
import {TabHeaderComponent} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EventModule,
    ToolsModule,
    IconModule,
  ],
  declarations: [
    TabsHeaderComponent,
    TabsContentComponent,
    TabHeaderComponent,
  ],
  exports: [
    TabsHeaderComponent,
    TabsContentComponent,
  ],
  entryComponents: [
    TabHeaderComponent,
  ]
})
export class TabsModule {
}
