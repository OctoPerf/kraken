import {Component} from '@angular/core';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faSignOutAlt, faUser} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {MENU_ICON} from 'projects/icon/src/lib/icons';

library.add(faSignOutAlt, faUser);

@Component({
  selector: 'lib-account-menu',
  templateUrl: './account-menu.component.html',
  styleUrls: ['./account-menu.component.scss']
})
export class AccountMenuComponent {

  readonly menuIcon = MENU_ICON;
  readonly accountIcon = new IconFa(faUser);
  readonly logoutIcon = new IconFa(faSignOutAlt);

  constructor(public security: SecurityService) {
  }

}
