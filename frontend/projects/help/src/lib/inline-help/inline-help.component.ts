import {Component, Input} from '@angular/core';
import {HELP_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'lib-inline-help',
  templateUrl: './inline-help.component.html',
  styleUrls: ['./inline-help.component.scss']
})
export class InlineHelpComponent {

  readonly helpIcon = HELP_ICON;

  @Input() tooltip: string;

}
