import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {
  SIDE_HEADER_DATA,
  TAB_HEADER_DATA,
  TabHeaderComponent
} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {IconFaCounter} from 'projects/icon/src/lib/icon-fa-counter';
import {Tab} from 'projects/tabs/src/lib/tab';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {Subscription} from 'rxjs';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {GitStatus} from 'projects/git/src/lib/entities/git-status';

@Component({
  selector: 'lib-git-status-tab-header',
  templateUrl: './git-status-tab-header.component.html',
  styleUrls: ['./git-status-tab-header.component.scss']
})
export class GitStatusTabHeaderComponent extends TabHeaderComponent implements OnInit, OnDestroy {

  public icon: IconFaCounter;
  private gitSubscription: Subscription;

  constructor(@Inject(TAB_HEADER_DATA) tab: Tab,
              @Inject(SIDE_HEADER_DATA) side: TabsSide,
              tabsService: TabsService,
              eventBus: EventBusService,
              public gitCommandService: GitCommandService) {
    super(tab, side, tabsService, eventBus);
  }

  ngOnInit() {
    this.icon = new IconFaCounter(this.tab.icon as IconFa, '', 'info');
    this.gitSubscription = this.gitCommandService.statusSubject.subscribe((status: GitStatus) => {
      let count = 0;
      if (status) {
        count += status.unmerged.length;
        count += status.renamedCopied.length;
        count += status.changed.length;
        count += status.ignored.length;
        count += status.untracked.length;
      }
      count = Math.min(count, 99);
      this.icon.content = count.toString();
    });
  }

  ngOnDestroy() {
    super.ngOnDestroy();
    this.gitSubscription.unsubscribe();
  }

}
