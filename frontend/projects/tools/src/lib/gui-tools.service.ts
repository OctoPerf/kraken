import {ElementRef, Injectable} from '@angular/core';
import {interval, Subscription} from 'rxjs';
import {finalize, map, take} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class GuiToolsService {

  private static readonly DELTA_HEIGHT = 50;
  private static readonly SCROLL_EASE_PERIOD = 10; // ms
  private static readonly SCROLL_EASE_STEPS = 25; // total animation duration is equal to period * steps

  private subscriptions: Map<string, Subscription> = new Map<string, Subscription>();

  public scrollTo(scrollableElement: ElementRef<HTMLElement>, getElement: () => Element | null): void {
    setTimeout(() => {
      const element = getElement();
      if (!element) {
        return;
      }
      const scrollHeight = scrollableElement.nativeElement.offsetHeight;
      const scrollTop = scrollableElement.nativeElement.getBoundingClientRect().top;
      const scrollBottom = scrollTop + scrollHeight;
      const elementTop = element.getBoundingClientRect().top;
      const elementBottom = element.getBoundingClientRect().bottom;
      const deltaBottom = scrollBottom - GuiToolsService.DELTA_HEIGHT - elementBottom;
      const deltaTop = elementTop - (scrollTop + GuiToolsService.DELTA_HEIGHT);
      if (deltaBottom < 0) {
        this.easeScroll(scrollableElement.nativeElement, Math.abs(deltaBottom));
      } else if (deltaTop < 0) {
        this.easeScroll(scrollableElement.nativeElement, -Math.abs(deltaTop));
      }
    });
  }

  private easeOutQuart(t: number): number {
    return 1 - (--t) * t * t * t;
  }

  private easeScroll(nativeElement: HTMLElement, delta: number) {
    const id = nativeElement.id;
    if (this.subscriptions.has(id)) {
      this.subscriptions.get(id).unsubscribe();
    }

    const start = nativeElement.scrollTop;
    const steps = GuiToolsService.SCROLL_EASE_STEPS;
    const period = GuiToolsService.SCROLL_EASE_PERIOD;

    const subscription = interval(period).pipe(take(steps),
      map(t => t / (steps - 1)), // set time in [0,1] interval
      map(this.easeOutQuart), // easing out
      map(x => start + (x * delta)),
      finalize(() => nativeElement.scrollTop = start + delta) // force end value when unsubscribed
    ).subscribe(value => nativeElement.scrollTop = value);

    this.subscriptions.set(id, subscription);
  }

  public copyToClipboard(text: string) {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = text;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
  }
}
