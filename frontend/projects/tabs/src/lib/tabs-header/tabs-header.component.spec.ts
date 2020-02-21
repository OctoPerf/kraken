import {async, ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';

import {TabsHeaderComponent} from './tabs-header.component';
import {TabsModule} from '../tabs.module';
import {Component, NgModule} from '@angular/core';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabsContentComponent} from 'projects/tabs/src/lib/tabs-content/tabs-content.component';
import {TabsContentInitializedEvent} from 'projects/tabs/src/lib/tabs-content/tabs-content-initialized-event';
import {TabsPosition} from 'projects/tabs/src/lib/tabs-position';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {tabsServiceSpy} from 'projects/tabs/src/lib/tabs.service.spec';
import {ComponentPortal} from '@angular/cdk/portal';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faQuestionCircle} from '@fortawesome/free-solid-svg-icons';
import {Tab} from 'projects/tabs/src/lib/tab';

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
  providers: [
    {provide: WindowService, useValue: windowSpy()}
  ]
})
class TestModule {
}

describe('TabsHeaderComponent', () => {
  let component: TabsHeaderComponent;
  let fixture: ComponentFixture<TabsHeaderComponent>;
  let eventBus: EventBusService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      providers: [
        {provide: TabsService, useValue: tabsServiceSpy()}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    eventBus = TestBed.inject(EventBusService);
    fixture = TestBed.createComponent(TabsHeaderComponent);
    component = fixture.componentInstance;
    component.tabs = [
      newTestTab(TestComponent),
      new Tab(new ComponentPortal(TestComponent), 'TestStart', new IconFa(faQuestionCircle), 'TEST', false, ['test'], TestComponent)
    ];
    component.side = TabsSide.TOP;
    component.position = TabsPosition.START;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init content on bus event', fakeAsync(() => {
    const content = new TabsContentComponent(null, eventBus);
    content.headerSide = TabsSide.TOP;
    content.headerPosition = TabsPosition.START;
    eventBus.publish(new TabsContentInitializedEvent(content));
    tick(1);
    expect(component.content).toBe(content);
  }));


  it('should (un)select', inject([LocalStorageService, EventBusService],
    (storage: LocalStorageService, bus: EventBusService) => {
      const content = new TabsContentComponent(storage, bus);
      content.tabs = component.tabs;
      component.content = content;
      fixture.detectChanges();
      fixture.nativeElement.querySelector('button').click();
      expect(component.content.selectedTab).toBe(component.tabs[0]);
      fixture.nativeElement.querySelector('button').click();
      expect(component.content.selectedTab).toBeNull();
      expect(TestBed.inject(WindowService).resizeNow).toHaveBeenCalled();
    }));

});
