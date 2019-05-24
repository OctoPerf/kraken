import * as _ from 'lodash';
import {Component, Injector, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {filter} from 'rxjs/internal/operators';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import {SIDE_HEADER_DATA, Tab, TAB_HEADER_DATA, TabHeaderComponent} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsPosition} from 'projects/tabs/src/lib/tabs-position';
import {TabsContentComponent} from 'projects/tabs/src/lib/tabs-content/tabs-content.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {TabsContentInitializedEvent} from 'projects/tabs/src/lib/tabs-content/tabs-content-initialized-event';
import {TabsAddedEvent} from 'projects/tabs/src/lib/tabs-added-event';

@Component({
  selector: 'lib-tabs-header',
  templateUrl: './tabs-header.component.html',
  styleUrls: ['./tabs-header.component.scss']
})
export class TabsHeaderComponent implements OnInit, OnDestroy {

  @Input() tabs: Tab[];
  @Input() side: TabsSide;
  @Input() position: TabsPosition;
  content: TabsContentComponent;
  portals: ComponentPortal<TabHeaderComponent>[];

  private subscription: Subscription;

  constructor(private injector: Injector,
              private eventBus: EventBusService,
              private window: WindowService) {
    this.subscription = this.eventBus.of<TabsContentInitializedEvent>(TabsContentInitializedEvent.CHANNEL)
      .pipe(filter(event => event.content.headerPosition === this.position && event.content.headerSide === this.side))
      .subscribe(event => setTimeout(() => this.content = event.content));
  }

  ngOnInit() {
    this.eventBus.publish(new TabsAddedEvent(this.side, this.position));
    this.portals = _.map(this.tabs, tab => {
      const injectorTokens = new WeakMap();
      injectorTokens.set(TAB_HEADER_DATA, tab);
      injectorTokens.set(SIDE_HEADER_DATA, this.side);
      return new ComponentPortal(tab.headerComponentRef, null, new PortalInjector(this.injector, injectorTokens));
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  selectTab(index: number) {
    const tab = this.tabs[index];
    if (this.content.selectedTab === tab) {
      this.content.unselectTab();
    } else {
      this.content.selectTab(index);
    }
    this.window.resizeNow();
  }

}
