import {ConnectProjectDirective} from 'projects/git/src/lib/git-project/connect-project.directive';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import {defaultDialogServiceSpy} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service.spec';
import {ConnectProjectDialogComponent} from 'projects/git/src/lib/git-project/connect-project-dialog/connect-project-dialog.component';
import {of} from 'rxjs';
import {testGitConfiguration} from 'projects/git/src/lib/entities/git-configuration.spec';
import SpyObj = jasmine.SpyObj;
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

describe('ConnectProjectDirective', () => {

  let directive: ConnectProjectDirective;
  let gitProject: SpyObj<GitProjectService>;
  let dialogs: SpyObj<DefaultDialogService>;

  beforeEach(() => {
    gitProject = gitProjectServiceSpy();
    dialogs = defaultDialogServiceSpy();
    directive = new ConnectProjectDirective(gitProject, dialogs);
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should open dialog', () => {
    const repositoryUrl = 'repositoryUrl';
    dialogs.open.and.returnValue(of(repositoryUrl));
    dialogs.waitFor.and.returnValue(of('ok'));
    gitProject.connect.and.returnValue(of(testGitConfiguration()));
    directive.onClick();
    expect(dialogs.open).toHaveBeenCalledWith(ConnectProjectDialogComponent, DialogSize.SIZE_MD);
    expect(dialogs.waitFor).toHaveBeenCalled();
    expect(gitProject.connect).toHaveBeenCalledWith(repositoryUrl);
  });

});
