import {Directive, HostListener} from '@angular/core';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {ConnectProjectDialogComponent} from 'projects/git/src/lib/git-project/connect-project-dialog/connect-project-dialog.component';
import {mergeMap} from 'rxjs/operators';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

@Directive({
  selector: '[libConnectProject]'
})
export class ConnectProjectDirective {

  constructor(private gitProject: GitProjectService,
              private dialogs: DefaultDialogService) {
  }

  @HostListener('click', ['$event']) onClick() {
    this.dialogs.open(ConnectProjectDialogComponent, DialogSize.SIZE_MD)
      .pipe(mergeMap((repositoryUrl: string) => this.dialogs.waitFor(this.gitProject.connect(repositoryUrl))
      )).subscribe();
  }

}
