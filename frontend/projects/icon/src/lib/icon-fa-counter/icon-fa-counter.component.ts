import {Component, Input, ViewEncapsulation} from '@angular/core';
import {IconFaCounter} from 'projects/icon/src/lib/icon-fa-counter';

@Component({
  selector: 'lib-icon-fa-counter',
  templateUrl: './icon-fa-counter.component.html',
  styleUrls: ['./icon-fa-counter.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class IconFaCounterComponent {

  @Input() icon: IconFaCounter;

}
