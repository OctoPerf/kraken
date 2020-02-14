import {ElementRef, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GuiToolsService {

  private static readonly DELTA_HEIGHT = 50;

  scrollTo(scrollableElement: ElementRef<HTMLElement>, getElement: () => Element): void {
    setTimeout(() => {
      const element = getElement();
      const scrollHeight = scrollableElement.nativeElement.offsetHeight;
      const scrollTop = scrollableElement.nativeElement.getBoundingClientRect().top;
      const scrollBottom = scrollTop + scrollHeight;
      const elementTop = element.getBoundingClientRect().top;
      const elementBottom = element.getBoundingClientRect().bottom;
      const deltaBottom = scrollBottom - GuiToolsService.DELTA_HEIGHT - elementBottom;
      const deltaTop = elementTop - (scrollTop + GuiToolsService.DELTA_HEIGHT);
      if (deltaBottom < 0) {
        scrollableElement.nativeElement.scrollTop += Math.abs(deltaBottom);
      } else if (deltaTop < 0) {
        scrollableElement.nativeElement.scrollTop -= Math.abs(deltaTop);
      }
    });
  }
}
