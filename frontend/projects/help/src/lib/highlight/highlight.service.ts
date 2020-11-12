import {Injectable, Injector} from '@angular/core';
import {Overlay, OverlayRef} from '@angular/cdk/overlay';
import {ComponentPortal} from '@angular/cdk/portal';
import {
  HIGHLIGHT_DURATION_DATA,
  HIGHLIGHT_RECT_DATA,
  HighlightBackdropComponent
} from 'projects/help/src/lib/highlight/highlight-backdrop/highlight-backdrop.component';

@Injectable({
  providedIn: 'root'
})
export class HighlightService {

  document = document;
  overlayRef: OverlayRef;

  constructor(
    private injector: Injector,
    private overlay: Overlay) {
    this.overlayRef = this.overlay.create({
      width: '100%',
      height: '100%',
    });
  }

  highlight(
    selector: string,
    duration = 800
  ) {
    // Min duration is 300ms because of fade in/out animations
    duration = Math.max(duration, 300);
    const element: HTMLElement = this.document.querySelector(selector);
    if (!element) {
      return;
    }
    const rect = element.getBoundingClientRect();
    const portal = new ComponentPortal(HighlightBackdropComponent, null,
      Injector.create({
        providers: [
          {provide: HIGHLIGHT_RECT_DATA, useValue: rect},
          {provide: HIGHLIGHT_DURATION_DATA, useValue: duration},
        ],
        parent: this.injector
      }));
    this.overlayRef.attach(portal);
    setTimeout(() => {
      this.overlayRef.detach();
    }, duration);
  }
}
