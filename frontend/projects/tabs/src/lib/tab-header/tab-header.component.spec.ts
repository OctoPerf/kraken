import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';
import {ComponentType} from '@angular/cdk/portal/typings/portal';
import {ComponentPortal} from '@angular/cdk/portal';
import {faQuestionCircle} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {Component} from '@angular/core';
import {
  SIDE_HEADER_DATA,
  TAB_HEADER_DATA,
  TabHeaderComponent
} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {Tab} from 'projects/tabs/src/lib/tab';
import {TabsModule} from 'projects/tabs/src/lib/tabs.module';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {tabsServiceSpy} from 'projects/tabs/src/lib/tabs.service.spec';
import Spy = jasmine.Spy;

library.add(faQuestionCircle);

export const newTestTab = (component: ComponentType<any>) => {
  return new Tab(new ComponentPortal(component), 'TestStart', new IconFa(faQuestionCircle), 'TEST', false, ['test']);
};

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('TabHeaderComponent', () => {
  let component: TabHeaderComponent;
  let fixture: ComponentFixture<TabHeaderComponent>;
  let testTab: Tab;

  beforeEach(async(() => {
    testTab = newTestTab(TestComponent);

    TestBed.configureTestingModule({
      imports: [TabsModule],
      declarations: [TestComponent],
      providers: [
        {provide: TAB_HEADER_DATA, useValue: testTab},
        {provide: SIDE_HEADER_DATA, useValue: TabsSide.LEFT},
        {provide: TabsService, useValue: tabsServiceSpy()},
      ]
    })
      .compileComponents();
  }));

  it('should create and select tab', inject([TabsService], (tabsService: TabsService) => {
    (tabsService.isSelected as Spy).and.returnValue(true);
    fixture = TestBed.createComponent(TabHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.state).toBe('selected');
  }));

  it('should create and not select tab', inject([EventBusService, TabsService], (eventBus: EventBusService, tabsService: TabsService) => {
    (tabsService.isSelected as Spy).and.returnValue(false);
    fixture = TestBed.createComponent(TabHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.state).toBe('');
    eventBus.publish(new TabSelectedEvent(testTab));
    expect(component.state).toBe('selected');
    eventBus.publish(new TabUnselectedEvent(testTab));
    expect(component.state).toBe('');
  }));
});
