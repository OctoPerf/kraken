import {Component, Input} from '@angular/core';
import {IconFa} from '../icon-fa';

@Component({
  selector: 'lib-icon-fa',
  templateUrl: './icon-fa.component.html',
})
export class IconFaComponent {

  @Input() icon: IconFa;

}
