import {Component, OnDestroy, OnInit} from '@angular/core';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import * as _ from 'lodash';
import {Subscription} from 'rxjs';
import {GitStatus} from 'projects/git/src/lib/entities/git-status';

@Component({
  selector: 'lib-git-status',
  templateUrl: './git-status.component.html',
  styleUrls: ['./git-status.component.scss']
})
export class GitStatusComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];
  public status: GitStatus;

  constructor(public gitProject: GitProjectService,
              private gitCommand: GitCommandService) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.gitCommand.statusSubject.subscribe(status => this.status = status));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }
}
