import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CurrentProjectComponent} from './current-project/current-project.component';

@NgModule({
  declarations: [
    CurrentProjectComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CurrentProjectComponent
  ]
})
export class CurrentProjectModule {
}
