import * as _ from 'lodash';
import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {state, style, transition, trigger, useAnimation} from '@angular/animations';
import {Subscription} from 'rxjs';
import {fadeInAnimation} from 'projects/commons/src/lib/animations';
import {Tab} from 'projects/tabs/src/lib/tab';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsPosition} from 'projects/tabs/src/lib/tabs-position';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {TabsContentInitializedEvent} from 'projects/tabs/src/lib/tabs-content/tabs-content-initialized-event';
import {TabUnselectedEvent} from 'projects/tabs/src/lib/tab-unselected-event';
import {CloseTabsEvent} from 'projects/tabs/src/lib/close-tabs-event';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

@Component({
  selector: 'lib-tabs-content',
  templateUrl: './tabs-content.component.html',
  styleUrls: ['./tabs-content.component.scss'],
  animations: [
    trigger(
      'tabState',
      [
        state('inactive', style({
          display: 'none',
        })),
        state('active', style({
          display: 'block',
        })),
        transition('inactive => active', useAnimation(fadeInAnimation, {params: {duration: '300ms'}})),
      ]
    )
  ],
})
export class TabsContentComponent implements OnInit, OnDestroy {

  @Input() id: string;
  @Input() defaultTabIndex: number;
  @Input() tabs: Tab[];
  @Input() headerSide: TabsSide;
  @Input() headerPosition: TabsPosition;
  @Output() tabSelected = new EventEmitter<[number, Tab]>();
  @Output() tabUnselected = new EventEmitter<void>();
  selectedTab: Tab;
  private subscriptions: Subscription[] = [];

  constructor(private storage: LocalStorageService,
              private eventBus: EventBusService) {
  }

  ngOnInit() {
    const index = this.storage.getNumber(this.id, this.defaultTabIndex);
    this.eventBus.of(CloseTabsEvent.CHANNEL).subscribe(this.unselectTab.bind(this));
    if (index !== -1) {
      this.selectedTab = this.tabs[index];
      this.eventBus.publish(new TabSelectedEvent(this.selectedTab));
    }
    _.forEach(this.tabs, (tab, i) => {
      _.forEach(tab.selectOn, busEventClass => {
        this.subscriptions.push(this.eventBus.of(busEventClass).subscribe(() => {
          if (tab !== this.selectedTab) {
            this.selectTab(i);
          }
        }));
      });
    });
    this.eventBus.publish(new TabsContentInitializedEvent(this));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  selectTab(index: number) {
    const tab = this.tabs[index];
    if (this.hasSelectedTab()) {
      this.eventBus.publish(new TabUnselectedEvent(this.selectedTab));
    }
    this.eventBus.publish(new TabSelectedEvent(tab));
    this.eventBus.publish(new SelectHelpEvent(tab.helpPageId));
    this.selectedTab = tab;
    this.storage.set(this.id, index);
    this.tabSelected.emit([index, tab]);
  }

  unselectTab() {
    if (this.hasSelectedTab()) {
      this.eventBus.publish(new TabUnselectedEvent(this.selectedTab));
      this.selectedTab = null;
      this.storage.set(this.id, -1);
      this.tabUnselected.emit();
    }
  }

  hasSelectedTab(): boolean {
    return !!this.selectedTab;
  }

}
