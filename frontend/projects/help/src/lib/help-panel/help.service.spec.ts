import {TestBed, inject} from '@angular/core/testing';
import {HelpService} from 'projects/help/src/lib/help-panel/help.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';


describe('HelpService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HelpService, EventBusService]
    });
  });

  it('should be created', inject([HelpService], (service: HelpService) => {
    expect(service).toBeTruthy();
    expect(service.lastPage.getValue()).toBe('HOME');
    service.ngOnDestroy();
  }));

  it('should store last pageId', inject([HelpService, EventBusService], (service: HelpService, eventBus: EventBusService) => {
    eventBus.publish(new OpenHelpEvent('TEST'));
    expect(service.lastPage.getValue()).toBe('TEST');
    eventBus.publish(new SelectHelpEvent('HOME'));
    expect(service.lastPage.getValue()).toBe('HOME');
  }));
});
