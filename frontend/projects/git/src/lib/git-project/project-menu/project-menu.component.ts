import {Component} from '@angular/core';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {MENU_ICON} from 'projects/icon/src/lib/icons';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faGitAlt} from '@fortawesome/free-brands-svg-icons/faGitAlt';
import {faUnlink} from '@fortawesome/free-solid-svg-icons/faUnlink';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faGitAlt, faUnlink);

@Component({
  selector: 'lib-project-menu',
  templateUrl: './project-menu.component.html',
  styleUrls: ['./project-menu.component.scss']
})
export class ProjectMenuComponent {

  readonly menuIcon = MENU_ICON;
  readonly connectIcon = new IconFa(faGitAlt, 'accent');
  readonly disconnectIcon = new IconFa(faUnlink, 'error');

  constructor(public gitProjectService: GitProjectService) {
  }

}
