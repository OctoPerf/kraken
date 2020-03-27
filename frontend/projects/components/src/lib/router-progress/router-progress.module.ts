import {NgModule} from '@angular/core';
import {RouterProgressComponent} from 'projects/components/src/lib/router-progress/router-progress.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {CommonModule} from '@angular/common';

@NgModule({
  imports: [
    CommonModule,
    MatProgressBarModule,
  ],
  declarations: [
    RouterProgressComponent,
  ],
  exports: [
    RouterProgressComponent,
  ],
})
export class RouterProgressModule {
}
