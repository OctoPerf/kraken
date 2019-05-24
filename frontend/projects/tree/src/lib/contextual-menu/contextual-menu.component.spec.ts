import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContextualMenuComponent} from './contextual-menu.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {TreeModule} from 'projects/tree/src/lib/tree.module';

describe('ContextualMenuComponent', () => {
  let component: ContextualMenuComponent;
  let fixture: ComponentFixture<ContextualMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, TreeModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContextualMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should open and close', () => {
    spyOn(component.contextMenu, 'openMenu').and.callThrough();
    spyOn(component.contextMenu, 'closeMenu').and.callThrough();
    const event = {clientX: 10, clientY: 10, preventDefault: jasmine.createSpy('preventDefault')};
    component.open(event as any);
    expect(event.preventDefault).toHaveBeenCalled();
    expect(component.contextMenu.openMenu).toHaveBeenCalled();
    expect(component.contextMenuPosition).toEqual({x: 10, y: 10});
    fixture.detectChanges();
    document.getElementsByClassName('cdk-overlay-backdrop')[0].dispatchEvent(new Event('contextmenu'));
    fixture.detectChanges();
    expect(component.contextMenu.closeMenu).toHaveBeenCalled();
  });
});
