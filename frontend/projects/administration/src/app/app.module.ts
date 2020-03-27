import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/administration/src/environments/environment';
import {AppRoutingModule} from 'projects/administration/src/app/app-routing.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';
import {RouterProgressModule} from 'projects/components/src/lib/router-progress/router-progress.module';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    RouterProgressModule,
    HighlightModule,
    SecurityModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
