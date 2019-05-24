import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ColorToFillClassPipe} from './color-to-fill-class.pipe';
import {ColorToTextClassPipe} from './color-to-text-class.pipe';
import {ColorToBackgroundClassPipe} from './color-to-background-class.pipe';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    ColorToFillClassPipe,
    ColorToTextClassPipe,
    ColorToBackgroundClassPipe,
  ],
  exports: [
    ColorToFillClassPipe,
    ColorToTextClassPipe,
    ColorToBackgroundClassPipe,
  ],
  providers: [
    ColorToFillClassPipe,
    ColorToTextClassPipe,
    ColorToBackgroundClassPipe,
  ]
})
export class ColorModule {
}
