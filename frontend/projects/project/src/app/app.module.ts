import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/project/src/environments/environment';
import {AppRoutingModule} from 'projects/project/src/app/app-routing.module';
import {RouterProgressModule} from 'projects/components/src/lib/router-progress/router-progress.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    RouterProgressModule,
    SecurityModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
