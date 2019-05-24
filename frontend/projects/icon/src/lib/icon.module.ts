import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IconComponent} from './icon/icon.component';
import {IconFaComponent} from './icon-fa/icon-fa.component';
import {IconDynamicComponent} from './icon-dynamic/icon-dynamic.component';
import {ColorModule} from 'projects/color/src/lib/color.module';
import {IconFaCounterComponent} from './icon-fa-counter/icon-fa-counter.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {IconFaAddonComponent} from './icon-fa-addon/icon-fa-addon.component';

@NgModule({
  imports: [
    CommonModule,
    FontAwesomeModule,
    ColorModule,
  ],
  declarations: [
    IconComponent,
    IconFaComponent,
    IconDynamicComponent,
    IconFaCounterComponent,
    IconFaAddonComponent,
  ],
  exports: [IconComponent]
})
export class IconModule {
}
