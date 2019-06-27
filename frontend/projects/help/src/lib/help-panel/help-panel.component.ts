import * as _ from 'lodash';
import {Component, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {faExternalLinkAlt} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {DomSanitizer, SafeResourceUrl, SafeUrl} from '@angular/platform-browser';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {HelpService} from 'projects/help/src/lib/help-panel/help.service';
import {SplitPanesDragStart} from 'projects/split/src/lib/split-panes/split-panes-drag-start';
import {SplitPanesDragStop} from 'projects/split/src/lib/split-panes/split-panes-drag-stop';
import {HELP_PAGES} from 'projects/help/src/lib/help-panel/help-pages';
import {IconFa} from 'projects/icon/src/lib/icon-fa';

library.add(faExternalLinkAlt);

@Component({
  selector: 'lib-help-panel',
  templateUrl: './help-panel.component.html',
  styleUrls: ['./help-panel.component.scss']
})
export class HelpPanelComponent implements OnDestroy {

  readonly newTabIcon = new IconFa(faExternalLinkAlt);

  pointerEvents = 'auto';
  subscriptions: Subscription[] = [];
  href: SafeUrl;
  src: SafeResourceUrl;

  constructor(public sanitizer: DomSanitizer,
              private configuration: ConfigurationService,
              eventBus: EventBusService,
              helpService: HelpService) {
    this.subscriptions.push(eventBus.of<SplitPanesDragStart>(SplitPanesDragStart.CHANNEL)
      .subscribe(() => this.pointerEvents = 'none'));
    this.subscriptions.push(eventBus.of<SplitPanesDragStop>(SplitPanesDragStop.CHANNEL)
      .subscribe(() => this.pointerEvents = 'auto'));
    this.subscriptions.push(helpService.lastPage.subscribe(pageId => this.page = pageId));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  set page(page: string) {
    const url = this.configuration.docUrl(HELP_PAGES[page]);
    this.href = this.sanitizer.bypassSecurityTrustUrl(url);
    this.src = this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

}
