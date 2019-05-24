import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule} from '@angular/common';

@NgModule({
  imports: [
    BrowserModule,
    CommonModule,
    BrowserAnimationsModule,
  ],
  exports: [
    CommonModule,
  ]
})
export class CoreModule {
}
