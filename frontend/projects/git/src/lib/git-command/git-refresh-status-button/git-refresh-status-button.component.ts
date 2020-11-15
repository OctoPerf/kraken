import {Component, OnInit} from '@angular/core';
import {REFRESH_ICON} from 'projects/icon/src/lib/icons';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';

@Component({
  selector: 'lib-git-refresh-status-button',
  templateUrl: './git-refresh-status-button.component.html',
  styleUrls: ['./git-refresh-status-button.component.scss']
})
export class GitRefreshStatusButtonComponent {

  readonly refreshIcon = REFRESH_ICON;
  public loading = false;

  constructor(private gitCommand: GitCommandService) {
  }

  refresh() {
    this.loading = true;
    this.gitCommand.status().subscribe(value => this.loading = false);
  }
}
