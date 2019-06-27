import * as _ from 'lodash';
import {Injectable, OnDestroy} from '@angular/core';
import {Tab} from 'projects/tabs/src/lib/tab';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';
import {Subscription} from 'rxjs';

@Injectable()
export class TabsService implements OnDestroy {

  selectedTabs: Tab[] = [];

  private subscriptions: Subscription[] = [];

  constructor(eventBus: EventBusService) {
    this.subscriptions.push(eventBus.of<TabSelectedEvent>(TabSelectedEvent.CHANNEL)
      .subscribe(event => this.selectedTabs.push(event.tab)));
    this.subscriptions.push(eventBus.of<TabUnselectedEvent>(TabUnselectedEvent.CHANNEL)
      .subscribe(event => _.pull(this.selectedTabs, event.tab)));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  isSelected(predicate: any): boolean {
    return _.findIndex(this.selectedTabs, predicate) !== -1;
  }
}
