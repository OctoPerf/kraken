import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {environment} from 'projects/administration/src/environments/environment';
import {AppRoutingModule} from 'projects/administration/src/app/app-routing.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    ComponentsModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
