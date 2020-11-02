import {NgModule} from '@angular/core';
import {GitFileStatusService} from 'projects/git/src/lib/git-file-status/git-file-status.service';


@NgModule({
  providers: [
    GitFileStatusService
  ],
})
export class GitFileStatusModule {
}
