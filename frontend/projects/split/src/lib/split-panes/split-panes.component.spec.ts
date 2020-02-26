import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SplitPanesComponent} from './split-panes.component';
import {SplitModule} from '../split.module';
import {Component, NgModule} from '@angular/core';
import {SplitPane} from '../split-pane';
import {ComponentPortal} from '@angular/cdk/portal';
import {SplitDrag} from './split-drag';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {eventSpy} from 'projects/commons/src/lib/mock/event.mock.spec';

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

@NgModule({
  imports: [CoreTestModule, SplitModule],
  declarations: [TestComponent],
  providers: [
    {provide: WindowService, useValue: windowSpy()}
  ]
})
class TestModule {
}

describe('SplitPanesComponent', () => {
  let component: SplitPanesComponent;
  let fixture: ComponentFixture<SplitPanesComponent>;
  let storage: LocalStorageService;
  let event: MouseEvent;

  beforeEach(async(() => {
    event = eventSpy();
    TestBed.configureTestingModule({
      imports: [TestModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    storage = TestBed.inject(LocalStorageService);
    storage.clear();
    fixture = TestBed.createComponent(SplitPanesComponent);
    component = fixture.componentInstance;
    component.id = 'test';
    component.directionId = 'horizontal';
    component.panes = [
      new SplitPane(new ComponentPortal(TestComponent), 20, 10),
      new SplitPane(new ComponentPortal(TestComponent), 30, 20),
      new SplitPane(new ComponentPortal(TestComponent), 50, 30),
    ];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.configurations).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 30, defaultSize: 30, minSize: 20},
      {visibleSize: 50, defaultSize: 50, minSize: 30}
    ]);
  });

  it('should cursorPressed', () => {
    spyOn(component.direction, 'eventToSize').and.returnValue(42);
    spyOn(component.direction, 'divToSize').and.returnValue(1337);
    component.cursorPressed(0, event);
    expect(component.drag).toEqual(new SplitDrag(42,
      0,
      1,
      20,
      30,
      1337,
      1337));
  });

  it('should cursorPressed with hidden pane', () => {
    spyOn(component.direction, 'eventToSize').and.returnValue(42);
    spyOn(component.direction, 'divToSize').and.returnValue(1337);
    component.configurations[1].visibleSize = 0;
    component.cursorPressed(0, event);
    expect(component.drag).toEqual(new SplitDrag(42,
      0,
      2,
      20,
      50,
      1337,
      1337));
  });

  it('should cursorDragged without drag', () => {
    const spy = spyOn(component.direction, 'eventToSize');
    component.cursorDragged(null);
    expect(spy).not.toHaveBeenCalled();
  });

  it('should cursorDragged', () => {
    component.drag = new SplitDrag(500,
      0,
      1,
      20,
      30,
      1000,
      1000);
    spyOn(component.direction, 'eventToSize').and.returnValue(400);
    component.cursorDragged(null);
    expect(component.configurations[0].visibleSize).toBeLessThan(20);
    expect(component.configurations[1].visibleSize).toBeGreaterThan(30);
  });

  it('should cursorDragged fail because of min size left', () => {
    component.drag = new SplitDrag(1000,
      0,
      1,
      20,
      30,
      1000,
      1000);
    spyOn(component.direction, 'eventToSize').and.returnValue(1);
    component.cursorDragged(null);
    expect(component.configurations[0].visibleSize).toBe(20);
    expect(component.configurations[1].visibleSize).toBe(30);
  });

  it('should cursorDragged fail because of min size right', () => {
    component.drag = new SplitDrag(1000,
      0,
      1,
      20,
      30,
      1000,
      1000);
    spyOn(component.direction, 'eventToSize').and.returnValue(1000);
    component.cursorDragged(null);
    expect(component.configurations[0].visibleSize).toBe(20);
    expect(component.configurations[1].visibleSize).toBe(30);
  });

  it('should stopDrag do nothing without drag', () => {
    const spy = spyOn(component, '_saveConfigurations');
    component.drag = null;
    component.stopDrag();
    expect(spy).not.toHaveBeenCalled();
  });

  it('should stopDrag hide all panes', () => {
    spyOn(component.paneHidden, 'emit');
    component.drag = new SplitDrag(1000,
      0,
      1,
      20,
      30,
      1000,
      1000);
    spyOn(component.direction, 'divToSize').and.returnValue(0);
    component.stopDrag();
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 0, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ]);
    expect(component.paneHidden.emit).toHaveBeenCalledTimes(3);
    expect(TestBed.inject(WindowService).resize).toHaveBeenCalled();
  });

  it('should stopDrag update sizes', () => {
    spyOn(component.paneHidden, 'emit');
    component.configurations =
      [
        {visibleSize: 10, defaultSize: 20, minSize: 10},
        {visibleSize: 35, defaultSize: 30, minSize: 20},
        {visibleSize: 55, defaultSize: 50, minSize: 30}
      ];
    component.drag = new SplitDrag(1000,
      0,
      1,
      20,
      30,
      1000,
      1000);
    spyOn(component.direction, 'divToSize').and.returnValue(42);
    component.stopDrag();
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 10, defaultSize: 20, minSize: 10},
      {visibleSize: 35, defaultSize: 30, minSize: 20},
      {visibleSize: 55, defaultSize: 50, minSize: 30}
    ]);
  });

  it('should hide', () => {
    spyOn(component.paneHidden, 'emit');
    spyOn(component.direction, 'divToSize').and.returnValue(42);
    component.hide(0);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 37.5, defaultSize: 30, minSize: 20},
      {visibleSize: 62.5, defaultSize: 50, minSize: 30}
    ]);
    component.hide(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 100, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ]);
    component.hide(0);
  });

  it('should show already visible pane', () => {
    const spy = spyOn(component, '_updateClosestConfiguration');
    component.show(0);
    expect(spy).not.toHaveBeenCalled();
  });

  it('should show restore size', () => {
    spyOn(component.paneShown, 'emit');
    component.configurations = [
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 37.5, defaultSize: 30, minSize: 20},
      {visibleSize: 62.5, defaultSize: 50, minSize: 30}
    ];
    component.show(0);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 30, defaultSize: 30, minSize: 20},
      {visibleSize: 50, defaultSize: 50, minSize: 30}
    ]);
    expect(component.paneShown.emit).toHaveBeenCalledWith([0, component.panes[0]]);
  });

  it('should show restore size bis', () => {
    component.configurations = [
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 100, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ];
    component.show(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 37.5, defaultSize: 30, minSize: 20},
      {visibleSize: 62.5, defaultSize: 50, minSize: 30}
    ]);
    component.show(0);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 30, defaultSize: 30, minSize: 20},
      {visibleSize: 50, defaultSize: 50, minSize: 30}
    ]);
  });

  it('should show restore size ter', () => {
    spyOn(component.paneShown, 'emit');
    component.configurations = [
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 37.5, defaultSize: 30, minSize: 10},
      {visibleSize: 62.5, defaultSize: 50, minSize: 30}
    ];
    component.show(0);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 17.5, defaultSize: 30, minSize: 10},
      {visibleSize: 62.5, defaultSize: 50, minSize: 30}
    ]);
    expect(component.paneShown.emit).toHaveBeenCalledWith([0, component.panes[0]]);
  });

  it('should show restore default', () => {
    component.configurations = [
      {visibleSize: 75, defaultSize: 20, minSize: 10},
      {visibleSize: 25, defaultSize: 60, minSize: 10},
      {visibleSize: 0, defaultSize: 20, minSize: 10}
    ];
    component.show(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 60, defaultSize: 60, minSize: 10},
      {visibleSize: 20, defaultSize: 20, minSize: 10}
    ]);
  });

  it('should show restore default bis', () => {
    component.configurations = [
      {visibleSize: 70, defaultSize: 20, minSize: 0},
      {visibleSize: 25, defaultSize: 60, minSize: 0},
      {visibleSize: 0, defaultSize: 20, minSize: 0}
    ];
    component.show(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 0},
      {visibleSize: 60, defaultSize: 60, minSize: 0},
      {visibleSize: 20, defaultSize: 20, minSize: 0}
    ]);
  });

  it('should hide all and show all', () => {
    component.hide(0);
    component.hide(1);
    component.hide(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 0, defaultSize: 20, minSize: 10},
      {visibleSize: 0, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ]);
    component.show(0);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 100, defaultSize: 20, minSize: 10},
      {visibleSize: 0, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ]);
    component.show(1);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 40, defaultSize: 20, minSize: 10},
      {visibleSize: 60, defaultSize: 30, minSize: 20},
      {visibleSize: 0, defaultSize: 50, minSize: 30}
    ]);
    component.show(2);
    expect(storage.getItem(component.id)).toEqual([
      {visibleSize: 20, defaultSize: 20, minSize: 10},
      {visibleSize: 30, defaultSize: 30, minSize: 20},
      {visibleSize: 50, defaultSize: 50, minSize: 30}
    ]);
  });
});
