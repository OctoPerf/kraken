import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/gatling/src/environments/environment';
import {AppRoutingModule} from 'projects/gatling/src/app/app-routing.module';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {RouterProgressModule} from 'projects/components/src/lib/router-progress/router-progress.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {ProjectIdHeaderInterceptor} from 'projects/commons/src/lib/config/project-id-header-interceptor.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    RouterProgressModule,
    HighlightModule,
    SecurityModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    RuntimeHostService,
    {provide: HTTP_INTERCEPTORS, useClass: ProjectIdHeaderInterceptor, multi: true},
  ]
})
export class AppModule {
}
