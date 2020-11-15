import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {CodeEditorComponent} from 'projects/editor/src/lib/code-editor/code-editor.component';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {Subscription} from 'rxjs';
import * as _ from 'lodash';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faUnlink} from '@fortawesome/free-solid-svg-icons/faUnlink';
import {library} from '@fortawesome/fontawesome-svg-core';
import {LOADING_ICON} from 'projects/icon/src/lib/icons';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {faLevelDownAlt} from '@fortawesome/free-solid-svg-icons/faLevelDownAlt';
import {RestServerError} from 'projects/commons/src/lib/config/rest-server-error';

library.add(faUnlink);

@Component({
  selector: 'lib-git-command',
  templateUrl: './git-command.component.html',
  styleUrls: ['./git-command.component.scss']
})
export class GitCommandComponent implements AfterViewInit, OnDestroy {

  readonly disconnectIcon = new IconFa(faUnlink, 'error');
  readonly executeIcon = new IconDynamic(new IconFa(faLevelDownAlt, 'accent', 'rotate-90'), {
    'loading': LOADING_ICON
  });

  @ViewChild(CodeEditorComponent) codeEditor: CodeEditorComponent;

  public input = '';
  public loading = false;

  private subscriptions: Subscription[] = [];

  constructor(public gitProject: GitProjectService,
              private gitCommand: GitCommandService) {
  }

  ngAfterViewInit() {
    this.subscriptions.push(this.gitCommand.logsSubject.subscribe(text => this.codeEditor.appendText(`${text}\n`)));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  public execute(): void {
    if (this.loading) {
      return;
    }
    this.loading = true;
    const command = this.input.startsWith('git ') ? this.input : `git ${this.input}`;
    this.codeEditor.appendText(`> ${command}\n`);
    this.input = '';
    this.gitCommand.execute(command)
      .subscribe(() => this.loading = false, (error: RestServerError) => {
        this.codeEditor.appendText(`${error.title}: ${error.message}\n${error.trace}`);
        this.loading = false;
      });
  }

}
