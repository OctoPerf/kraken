import {Component, Input} from '@angular/core';
import {faChevronDown, faChevronRight} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {IconFa} from 'projects/icon/src/lib/icon-fa';

library.add(faChevronDown, faChevronRight);

@Component({
  selector: 'lib-toggle-button',
  templateUrl: './toggle-button.component.html',
  styleUrls: ['./toggle-button.component.scss'],
  animations: [
    trigger(
      'state',
      [
        state('expanded', style({transform: 'rotate(90deg)'})),
        state('collapsed', style({transform: 'rotate(0)'})),
        transition('expanded => collapsed', animate('100ms ease-out')),
        transition('collapsed => expanded', animate('100ms ease-in'))
      ]
    )
  ],
})
export class ToggleButtonComponent {

  readonly expandIcon = new IconFa(faChevronRight);

  state: 'expanded' | 'collapsed';

  @Input() set expanded(expanded: boolean) {
    this.state = expanded ? 'expanded' : 'collapsed';
  }
}
