import {Component, Input, OnInit} from '@angular/core';
import {Icon} from '../icon';
import {IconDynamic} from '../icon-dynamic';

@Component({
  selector: 'lib-icon-dynamic',
  templateUrl: './icon-dynamic.component.html',
})
export class IconDynamicComponent implements OnInit {

  current: Icon;
  _state: string;

  @Input() icon: IconDynamic;

  @Input() set state(state: string) {
    this._state = state;
    this._updateCurrent(state);
  }

  ngOnInit() {
    this._updateCurrent(this._state);
  }

  _updateCurrent(state: string) {
    this.current = this.icon.stateIcons[state] || this.icon.defaultIcon;
  }
}
