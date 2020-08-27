import {Component} from '@angular/core';
import {ADD_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent {

  readonly newIcon = ADD_ICON;

}
