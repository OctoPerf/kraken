import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {StorageDialogsModule} from 'projects/storage/src/lib/storage-dialogs/storage-dialogs.module';
import {environment} from 'projects/administration/src/environments/environment';
import {AppRoutingModule} from 'projects/administration/src/app/app-routing.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    ConfigurationModule.forRoot(environment),
    AppRoutingModule,
    ComponentsModule,
    // /!\ Might be a bug in angular-material because with lazy loaded modules,
    // the dialog do not work (error in console that tells us to add the components to entryComponents)
    // That's why me must import the dialog module here (not lazy).
    // Thanks to https://stackoverflow.com/questions/41519481/angular2-material-dialog-has-issues-did-you-add-it-to-ngmodule-entrycomponent
    StorageDialogsModule,
    DialogModule,
    HighlightModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
