import {ComponentType, Portal} from '@angular/cdk/portal';
import {Icon} from 'projects/icon/src/lib/icon';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

export class Tab {

  constructor(
    public portal: Portal<any>,
    public label: string,
    public icon: Icon,
    public helpPageId: HelpPageId = null,
    public keepContent: boolean = true,
    public selectOn: string[] = [],
    public headerComponentRef?: ComponentType<any>,
  ) {
  }
}
