import {inject, TestBed} from '@angular/core/testing';

import {Component} from '@angular/core';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';


export const tabsServiceSpy = () => jasmine.createSpyObj('TabsService', ['isSelected']);

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('TabsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TabsService,
        EventBusService
      ],
      declarations: [TestComponent]
    });
  });

  it('should be created', inject([TabsService], (service: TabsService) => {
    expect(service).toBeTruthy();
    service.ngOnDestroy();
  }));

  it('should (un)select tab', inject([TabsService, EventBusService], (service: TabsService, eventBus: EventBusService) => {
    const tab = newTestTab(TestComponent);
    eventBus.publish(new TabSelectedEvent(tab));
    expect(service.isSelected({label: tab.label})).toBe(true);
    expect(service.isSelected({label: 'someothershit'})).toBe(false);
    eventBus.publish(new TabUnselectedEvent(tab));
    expect(service.isSelected({label: tab.label})).toBe(false);
  }));
});
