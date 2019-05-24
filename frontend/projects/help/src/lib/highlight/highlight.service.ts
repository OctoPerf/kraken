import {Injectable, Injector} from '@angular/core';
import {Overlay} from '@angular/cdk/overlay';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import {OverlayRef} from '@angular/cdk/overlay/typings/overlay-ref';
import {
  HIGHLIGHT_DURATION_DATA,
  HIGHLIGHT_RECT_DATA, HighlightBackdropComponent
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
    const injectorTokens = new WeakMap();
    injectorTokens.set(HIGHLIGHT_RECT_DATA, rect);
    injectorTokens.set(HIGHLIGHT_DURATION_DATA, duration);
    const portal = new ComponentPortal(HighlightBackdropComponent, null, new PortalInjector(this.injector, injectorTokens));
    this.overlayRef.attach(portal);
    setTimeout(() => {
      this.overlayRef.detach();
    }, duration);
  }
}
