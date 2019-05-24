import {Component, Input} from '@angular/core';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';

@Component({
  selector: 'lib-icon-fa-addon',
  templateUrl: './icon-fa-addon.component.html',
})
export class IconFaAddonComponent {

  @Input() icon: IconFaAddon;

}
