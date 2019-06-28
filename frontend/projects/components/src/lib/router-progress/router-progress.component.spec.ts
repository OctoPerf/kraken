import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router} from '@angular/router';
import {of} from 'rxjs';
import {RouterProgressComponent} from 'projects/components/src/lib/router-progress/router-progress.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {routerSpy} from 'projects/commons/src/lib/mock/router.mock.spec';

describe('RouterProgressComponent', () => {
  let component: RouterProgressComponent;
  let fixture: ComponentFixture<RouterProgressComponent>;
  let router: any;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RouterProgressComponent],
      imports: [VendorsModule, CoreTestModule],
      providers: [
        {provide: Router, useValue: routerSpy()}
      ]
    })
      .compileComponents();
    router = TestBed.get(Router);
  }));

  it('should update loading state true', () => {
    router.events = of(new NavigationStart(null, null));
    fixture = TestBed.createComponent(RouterProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component.loading).toBe(true);
  });

  it('should update loading state false', () => {
    router.events = of(
      new NavigationEnd(null, null, null),
      new NavigationCancel(null, null, null),
      new NavigationError(null, null, null),
    );
    fixture = TestBed.createComponent(RouterProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component.loading).toBe(false);
  });

  it('should update loading state false', fakeAsync(() => {
    router.events = of(
      new NavigationEnd(null, null, null),
      new NavigationCancel(null, null, null),
      new NavigationError(null, null, null),
    );
    const spyHTML: any = {outerHTML: 'someText', setAttribute: jasmine.createSpy('setAttribute')};
    spyOn(document, 'getElementById').and.returnValue(spyHTML);
    fixture = TestBed.createComponent(RouterProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component.loading).toBe(false);
    tick(600);
    expect(spyHTML.outerHTML).toBe('');
    expect(spyHTML.setAttribute).toHaveBeenCalledWith('class', 'fade-out');
  }));
});
