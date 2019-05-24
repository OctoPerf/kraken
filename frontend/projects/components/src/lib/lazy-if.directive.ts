import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[libLazyIf]'
})
export class LazyIfDirective {

  private loaded = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef) {
  }

  @Input() set libLazyIf(condition: boolean) {
    if (condition && !this.loaded) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.loaded = true;
    }
  }

}
