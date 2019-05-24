import {NgModule} from '@angular/core';
import {MaterialModule} from './material/material.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
  ],
  exports: [
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
  ],
})
export class VendorsModule {
}
