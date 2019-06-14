import {Component, Inject, InjectionToken, OnDestroy} from '@angular/core';
import {ComponentType, Portal} from '@angular/cdk/portal';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {Icon} from 'projects/icon/src/lib/icon';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {filter} from 'rxjs/operators';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {Subscription} from 'rxjs';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';

export const TAB_HEADER_DATA = new InjectionToken<Tab>('TabHeaderData');
export const SIDE_HEADER_DATA = new InjectionToken<TabsSide>('SideHeaderData');

@Component({
  selector: 'lib-tab-header',
  templateUrl: './tab-header.component.html',
  styleUrls: ['./tab-header.component.scss']
})
export class TabHeaderComponent implements OnDestroy {

  public state: '' | 'selected';
  private selectedSubscription: Subscription;
  private unselectedSubscription: Subscription;

  constructor(@Inject(TAB_HEADER_DATA) public tab: Tab,
              @Inject(SIDE_HEADER_DATA) public side: TabsSide,
              private tabsService: TabsService,
              private eventBus: EventBusService) {
    this.state = tabsService.isSelected(this.tab) ? 'selected' : '';
    this.selectedSubscription = this.eventBus.of<TabSelectedEvent>(TabSelectedEvent.CHANNEL)
      .pipe(filter(event => event.tab === this.tab))
      .subscribe(() => this.state = 'selected');
    this.unselectedSubscription = this.eventBus.of<TabUnselectedEvent>(TabUnselectedEvent.CHANNEL)
      .pipe(filter(event => event.tab === this.tab))
      .subscribe(() => this.state = '');
  }

  ngOnDestroy() {
    this.selectedSubscription.unsubscribe();
    this.unselectedSubscription.unsubscribe();
  }

}

export class Tab {

  constructor(
    public portal: Portal<any>,
    public label: string,
    public icon: Icon,
    public helpPageId: HelpPageId = null,
    public keepContent: boolean = true,
    public selectOn: string[] = [],
    public headerComponentRef: ComponentType<TabHeaderComponent> = TabHeaderComponent,
  ) {
  }
}
