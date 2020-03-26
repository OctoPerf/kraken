import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {environment} from 'projects/administration/src/environments/environment';
import {AppRoutingModule} from 'projects/administration/src/app/app-routing.module';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    // TODO try to load these modules only in the workspace
    ComponentsModule,
    HighlightModule,
    SecurityModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
