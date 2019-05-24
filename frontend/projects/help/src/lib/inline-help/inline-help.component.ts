import {Component, Input} from '@angular/core';
import {faQuestionCircle} from '@fortawesome/free-regular-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {IconFa} from 'projects/icon/src/lib/icon-fa';

library.add(faQuestionCircle);

@Component({
  selector: 'lib-inline-help',
  templateUrl: './inline-help.component.html',
  styleUrls: ['./inline-help.component.scss']
})
export class InlineHelpComponent {

  readonly helpIcon = new IconFa(faQuestionCircle, 'accent');

  @Input() tooltip: string;

}
