import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/gatling/src/environments/environment';
import {AppRoutingModule} from 'projects/gatling/src/app/app-routing.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {AnalysisModule} from 'projects/analysis/src/lib/analysis.module';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AnalysisModule, // For header interceptor
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    ComponentsModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    RuntimeHostService
  ]
})
export class AppModule {
}
