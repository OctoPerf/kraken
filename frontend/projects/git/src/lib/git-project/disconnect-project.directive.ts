import {Directive, HostListener} from '@angular/core';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {mergeMap} from 'rxjs/operators';

@Directive({
  selector: '[libDisconnectProject]'
})
export class DisconnectProjectDirective {

  constructor(private gitProjectService: GitProjectService,
              private dialogs: DefaultDialogService) {
  }

  @HostListener('click', ['$event']) onClick($event) {
    this.dialogs.confirm('Disconnect fom Git', 'Are you sure you want to disconnect the current project from Git?', $event.ctrlKey)
      .pipe(mergeMap(value => this.dialogs.waitFor(this.gitProjectService.disconnect())))
      .subscribe();
  }
}
