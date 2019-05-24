import {CoreModule} from './core.module';
import {NgModule} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CommonModule} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

@NgModule({
  imports: [
    // Do Not Import ConfigurationModule to prevent initialization
    BrowserModule,
    CommonModule,
    BrowserAnimationsModule,
    HttpClientTestingModule,
    RouterTestingModule,
  ],
  exports: [
    CoreModule,
  ],
  providers: [
    {provide: ConfigurationService, useValue: configurationServiceMock()}
  ]
})
export class CoreTestModule {
}

describe('CoreModule', () => {
  let coreModule: CoreModule;

  beforeEach(() => {
    coreModule = new CoreModule();
  });

  it('should create an instance', () => {
    expect(coreModule).toBeTruthy();
  });
});
