import {Portal} from '@angular/cdk/portal';

export class SplitPane {
  constructor(
    public portal: Portal<any>,
    public defaultSize: number,
    public minSize = 0,
    public initSize = -1,
  ) {
    console.assert(minSize >= 0, 'minSize => 0');
    console.assert(minSize <= 100, 'minSize <== 100');
    if (this.initSize === -1) {
      this.initSize = this.defaultSize;
    }
  }
}
