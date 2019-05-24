import {Component} from '@angular/core';
import {
  faCaretRight,
  faCloudDownloadAlt,
  faCogs,
  faCreditCard,
  faHome,
  faSignOutAlt,
  faUserEdit,
  faUserShield
} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faCaretRight,
  faCogs,
  faCloudDownloadAlt,
  faCreditCard,
  faHome, faSignOutAlt,
  faUserEdit,
  faUserShield);

@Component({
  selector: 'lib-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  readonly projectsIcon = faHome;

}
