import {DisconnectProjectDirective} from 'projects/git/src/lib/git-project/disconnect-project.directive';
import {ConnectProjectDirective} from 'projects/git/src/lib/git-project/connect-project.directive';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import {defaultDialogServiceSpy} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service.spec';
import {of} from 'rxjs';
import {testGitConfiguration} from 'projects/git/src/lib/entities/git-configuration.spec';
import {ConnectProjectDialogComponent} from 'projects/git/src/lib/git-project/connect-project-dialog/connect-project-dialog.component';
import SpyObj = jasmine.SpyObj;

describe('DisconnectProjectDirective', () => {

  let directive: DisconnectProjectDirective;
  let gitProject: SpyObj<GitProjectService>;
  let dialogs: SpyObj<DefaultDialogService>;

  beforeEach(() => {
    gitProject = gitProjectServiceSpy();
    dialogs = defaultDialogServiceSpy();
    directive = new DisconnectProjectDirective(gitProject, dialogs);
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should open dialog', () => {
    dialogs.confirm.and.returnValue(of(null));
    dialogs.waitFor.and.returnValue(of('ok'));
    gitProject.disconnect.and.returnValue(of(null));
    directive.onClick({ctrlKey: true});
    expect(dialogs.confirm).toHaveBeenCalled();
    expect(dialogs.waitFor).toHaveBeenCalled();
    expect(gitProject.disconnect).toHaveBeenCalled();
  });
});
