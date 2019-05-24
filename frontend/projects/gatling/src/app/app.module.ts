import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {CoreModule} from 'projects/commons/src/lib/core/core.module';
import {ConfigurationModule} from 'projects/commons/src/lib/config/configuration.module';
import {environment} from 'projects/gatling/src/environments/environment';
import {AppRoutingModule} from 'projects/gatling/src/app/app-routing.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {StorageDialogsModule} from 'projects/storage/src/lib/storage-dialogs/storage-dialogs.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {DockerDialogsModule} from 'projects/docker/src/lib/docker-dialogs/docker-dialogs.module';
import {CommandDialogsModule} from 'projects/command/src/lib/command-dialogs/command-dialogs.module';
import {SimulationDialogsModule} from 'projects/gatling/src/app/simulations/simulation-dialogs/simulation-dialogs.module';
import {AnalysisModule} from 'projects/analysis/src/lib/analysis.module';
import {CompareModule} from 'projects/analysis/src/lib/results/debug/compare/compare.module';

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
    // /!\ Might be a bug in angular-material because with lazy loaded modules,
    // the dialog do not work (error in console that tells us to add the components to entryComponents)
    // That's why me must import the dialog module here (not lazy).
    // Thanks to https://stackoverflow.com/questions/41519481/angular2-material-dialog-has-issues-did-you-add-it-to-ngmodule-entrycomponent
    StorageDialogsModule,
    DialogModule,
    HighlightModule,
    DockerDialogsModule,
    CommandDialogsModule,
    SimulationDialogsModule,
    CompareModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
