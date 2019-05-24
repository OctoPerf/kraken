import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, Subscription} from 'rxjs';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {HelpEvent} from 'projects/help/src/lib/help-panel/help-event';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

@Injectable({
  providedIn: 'root'
})
export class HelpService implements OnDestroy {

  lastPage: BehaviorSubject<HelpPageId>;

  private subscription: Subscription;

  constructor(eventBus: EventBusService) {
    this.lastPage = new BehaviorSubject<HelpPageId>('HOME');
    this.subscription = eventBus.of<HelpEvent>(OpenHelpEvent.CHANNEL, SelectHelpEvent.CHANNEL).subscribe(event => {
      this.lastPage.next(event.pageId);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
