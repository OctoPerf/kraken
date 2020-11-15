import {Component} from '@angular/core';
import {ADD_ICON} from 'projects/icon/src/lib/icons';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faGitAlt} from '@fortawesome/free-brands-svg-icons/faGitAlt';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faGitAlt);

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent {

  readonly newIcon = ADD_ICON;
  readonly importIcon = new IconFa(faGitAlt);

}
