import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';

import {Component, NgModule} from '@angular/core';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {TabsModule} from 'projects/tabs/src/lib/tabs.module';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {TabsContentComponent} from 'projects/tabs/src/lib/tabs-content/tabs-content.component';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import Spy = jasmine.Spy;
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

@NgModule({
  imports: [CoreTestModule, TabsModule],
  declarations: [TestComponent],
  entryComponents: [
    TestComponent,
  ],
})
class TestModule {
}

class TestEvent extends BusEvent {
  constructor() {
    super('test');
  }
}

describe('TabsContentComponent', () => {
  let component: TabsContentComponent;
  let fixture: ComponentFixture<TabsContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      providers: [
        {provide: LocalStorageService, useValue: localStorageServiceSpy()}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabsContentComponent);
    component = fixture.componentInstance;
    component.id = 'test';
    component.tabs = [
      newTestTab(TestComponent),
    ];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fire _selection event on creation', inject([EventBusService], (eventBus: EventBusService) => {
    const publish = spyOn(eventBus, 'publish');
    component.defaultTabIndex = 0;
    component.ngOnInit();
    expect(publish).toHaveBeenCalledWith(new TabSelectedEvent(component.selectedTab));
  }));

  it('should selectOn', inject([EventBusService], (service: EventBusService) => {
    component.selectedTab = null;
    const spy = spyOn(component, 'selectTab');
    service.publish(new TestEvent());
    expect(spy).toHaveBeenCalledWith(0);
  }));

  it('should not selectOn', inject([EventBusService], (service: EventBusService) => {
    component.selectedTab = component.tabs[0];
    const spy = spyOn(component, 'selectTab');
    service.publish(new TestEvent());
    expect(spy).not.toHaveBeenCalled();
  }));

  it('should load conf from storage', inject([LocalStorageService], (storage: LocalStorageService) => {
    (storage.getNumber as Spy).and.returnValue(0);
    component.ngOnInit();
    expect(component.selectedTab).toBe(component.tabs[0]);
  }));

  it('should load default conf', inject([LocalStorageService], (storage: LocalStorageService) => {
    (storage.getNumber as Spy).and.returnValue(-1);
    component.ngOnInit();
    expect(component.selectedTab).toBeUndefined();
  }));

  it('should selectTab', inject([LocalStorageService, EventBusService], (storage: LocalStorageService, eventBus: EventBusService) => {
    const publish = spyOn(eventBus, 'publish');
    const emit = spyOn(component.tabSelected, 'emit');
    component.selectTab(0);
    expect(component.selectedTab).toBe(component.tabs[0]);
    expect(storage.set).toHaveBeenCalledWith(component.id, 0);
    expect(emit).toHaveBeenCalledWith([0, component.tabs[0]]);
    expect(publish).toHaveBeenCalledWith(new TabSelectedEvent(component.selectedTab));
    expect(publish).toHaveBeenCalledWith(new SelectHelpEvent('TEST'));
  }));

  it('should other selectTab', inject([LocalStorageService, EventBusService], (storage: LocalStorageService, eventBus: EventBusService) => {
    const publish = spyOn(eventBus, 'publish');
    const emit = spyOn(component.tabSelected, 'emit');
    const otherTab = newTestTab(TestComponent);
    component.selectedTab = otherTab;
    component.selectTab(0);
    expect(component.selectedTab).toBe(component.tabs[0]);
    expect(storage.set).toHaveBeenCalledWith(component.id, 0);
    expect(emit).toHaveBeenCalledWith([0, component.tabs[0]]);
    expect(publish).toHaveBeenCalledWith(new TabSelectedEvent(component.tabs[0]));
    expect(publish).toHaveBeenCalledWith(new TabUnselectedEvent(otherTab));
  }));

  it('should unselectTab', inject([LocalStorageService, EventBusService], (storage: LocalStorageService, eventBus: EventBusService) => {
    const publish = spyOn(eventBus, 'publish');
    const tab = component.selectedTab = component.tabs[0];
    const emit = spyOn(component.tabUnselected, 'emit');
    component.unselectTab();
    expect(component.selectedTab).toBeNull();
    expect(storage.set).toHaveBeenCalledWith(component.id, -1);
    expect(emit).toHaveBeenCalled();
    expect(publish).toHaveBeenCalledWith(new TabUnselectedEvent(tab));
  }));

  it('should unselectTab do nothing', inject([LocalStorageService], (storage: LocalStorageService) => {
    component.selectedTab = null;
    const emit = spyOn(component.tabUnselected, 'emit');
    component.unselectTab();
    expect(emit).not.toHaveBeenCalled();
  }));
});
