import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/gatling/src/environments/environment';
import {AppRoutingModule} from 'projects/gatling/src/app/app-routing.module';
import {AnalysisModule} from 'projects/analysis/src/lib/analysis.module';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {RouterProgressModule} from 'projects/components/src/lib/router-progress/router-progress.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AnalysisModule, // For header interceptor
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    RouterProgressModule,
    HighlightModule,
    SecurityModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    RuntimeHostService
  ]
})
export class AppModule {
}
