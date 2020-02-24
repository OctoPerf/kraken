import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContextualMenuComponent} from './contextual-menu.component';

describe('ContextualMenuComponent', () => {
  let component: ContextualMenuComponent;
  let fixture: ComponentFixture<ContextualMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ContextualMenuComponent],
    }).overrideTemplate(ContextualMenuComponent, '')
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
    const offEvent = {preventDefault: jasmine.createSpy('preventDefault')};
    spyOn(document, 'getElementsByClassName').and.returnValue([{
      addEventListener: (eventType: string, callback: (event: any) => void) => {
        callback(offEvent);
      },
      dispatchEvent: jasmine.createSpy('dispatchEvent'),
    }] as any);
    component.contextMenu = jasmine.createSpyObj('contextMenu', ['openMenu', 'closeMenu']);
    const event = {clientX: 10, clientY: 10, preventDefault: jasmine.createSpy('preventDefault')};
    component.open(event as any);
    expect(event.preventDefault).toHaveBeenCalled();
    expect(component.contextMenu.openMenu).toHaveBeenCalled();
    expect(component.contextMenuPosition).toEqual({x: 10, y: 10});
    fixture.detectChanges();
    document.getElementsByClassName('cdk-overlay-backdrop')[0].dispatchEvent(new Event('contextmenu'));
    fixture.detectChanges();
    expect(component.contextMenu.closeMenu).toHaveBeenCalled();
    expect(offEvent.preventDefault).toHaveBeenCalled();
  });
});
