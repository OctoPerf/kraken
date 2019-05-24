import {Component, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsAddedEvent} from 'projects/tabs/src/lib/tabs-added-event';

@Component({
  selector: 'lib-full-page',
  templateUrl: './full-page.component.html',
  styleUrls: ['./full-page.component.scss'],
})
export class FullPageComponent implements OnDestroy {

  tabsTop = false;
  tabsRight = false;
  tabsBottom = false;
  tabsLeft = false;
  private subscription: Subscription;

  constructor(private eventBus: EventBusService) {
    const sideHandlers = {};
    sideHandlers[TabsSide.TOP] = () => {
      this.tabsTop = true;
    };
    sideHandlers[TabsSide.RIGHT] = () => {
      this.tabsRight = true;
    };
    sideHandlers[TabsSide.BOTTOM] = () => {
      this.tabsBottom = true;
    };
    sideHandlers[TabsSide.LEFT] = () => {
      this.tabsLeft = true;
    };

    this.subscription = this.eventBus.of<TabsAddedEvent>(TabsAddedEvent.CHANNEL).subscribe((event) => {
      setTimeout(() => {
        sideHandlers[event.side]();
      });
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
