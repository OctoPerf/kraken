import {LazyIfDirective} from './lazy-if.directive';
import {Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import SpyObj = jasmine.SpyObj;

describe('LazyIfDirective', () => {

  let directive: LazyIfDirective;
  let viewContainer: SpyObj<ViewContainerRef>;
  let templateRef: SpyObj<TemplateRef<any>>;

  beforeEach(() => {
    viewContainer = jasmine.createSpyObj('viewContainer', ['createEmbeddedView']);
    templateRef = jasmine.createSpyObj('templateRef', ['nope']);
    directive = new LazyIfDirective(templateRef, viewContainer);
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should lazy load', () => {
    directive.libLazyIf = true;
    directive.libLazyIf = true;
    expect(viewContainer.createEmbeddedView).toHaveBeenCalledWith(templateRef);
    expect(viewContainer.createEmbeddedView).toHaveBeenCalledTimes(1);
    expect(directive).toBeTruthy();
  });

});
