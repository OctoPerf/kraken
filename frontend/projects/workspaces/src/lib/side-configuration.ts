import {Portal} from '@angular/cdk/portal';
import {EMPTY_TABS_CONFIG, TabsConfiguration} from 'projects/workspaces/src/lib/tabs-configuration';
import {SplitPane} from 'projects/split/src/lib/split-pane';

export class SideConfiguration {
  constructor(
    public start: TabsConfiguration,
    public end: TabsConfiguration,
    public defaultSize: number,
    public minSize: number = 0,
  ) {
    console.assert(this.empty || (start.defaultSize + end.defaultSize === 100),
      `Side configuration size sum must be 100 = ${start.defaultSize} + ${end.defaultSize}`);
  }

  get empty(): boolean {
    return this.start.empty && this.end.empty;
  }

  get initSize(): number {
    return this.start.index === -1 && this.end.index === -1 ? 0 : this.defaultSize;
  }

  toSplitPane(portal: Portal<any>): SplitPane {
    return new SplitPane(portal, this.defaultSize, this.minSize, this.initSize);
  }
}

export const EMPTY_SIDE_CONFIG = new SideConfiguration(EMPTY_TABS_CONFIG, EMPTY_TABS_CONFIG, 0);
