import {ElementRef, Injectable, ViewChild} from '@angular/core';
import * as _ from 'lodash';

@Injectable()
export class ScrollPositionComponent {

  // @ViewChild('scrollableTree', {static: false}) scrollableTree: ElementRef;

  public upScrollPosition(): void {
    const els = document.getElementsByClassName('scrollable background-background');
    _.forEach(els, (el) => {
      el.scrollTop -= 26;
    });
    // this.scrollableTree.nativeElement.scrollTop -= 20;
    // -= 20;
  }

  public downScrollPosition(): void {
    const els = document.getElementsByClassName('scrollable background-background');
    _.forEach(els, (el) => {
      el.scrollTop += 26;
    });

    // this.scrollableTree.nativeElement.scrollTop -= 20;
    // Top += 20;
  }
}
