import {APP_INITIALIZER, ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConfigurationService, loadConfiguration} from './configuration.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ErrorInterceptor} from './error-interceptor.service';
import {EventModule} from 'projects/event/src/lib/event.module';
import {ENVIRONMENT} from 'projects/commons/src/lib/config/configuration-environment';
import {ExecutorApplicationIdService} from 'projects/commons/src/lib/config/executor-application-id.service';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    EventModule,
  ],
  exports: [
    HttpClientModule,
  ],
  providers: [
    {provide: APP_INITIALIZER, useFactory: loadConfiguration, deps: [ConfigurationService], multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ExecutorApplicationIdService, multi: true},
  ]
})
export class ConfigurationModule {
  public static forRoot(environment: any): ModuleWithProviders {
    return {
      ngModule: ConfigurationModule,
      providers: [
        {
          provide: ENVIRONMENT,
          useValue: environment
        },
      ]
    };
  }
}
