import {Component} from '@angular/core';
import {CLIPBOARD_ICON} from 'projects/icon/src/lib/icons';
import {GitUserService} from 'projects/git/src/lib/git-user/git-user.service';

@Component({
  selector: 'lib-ssh-public-key',
  templateUrl: './ssh-public-key.component.html',
  styleUrls: ['./ssh-public-key.component.scss']
})
export class SshPublicKeyComponent {

  readonly copyToClipboardIcon = CLIPBOARD_ICON;

  publicKey: string;
  panelOpenState = false;

  constructor(gitUserService: GitUserService) {
    gitUserService.publicKey().subscribe(value => this.publicKey = value);
  }

}
