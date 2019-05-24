import {Component, Inject, OnDestroy} from '@angular/core';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {BehaviorSubject, Subscription} from 'rxjs';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faLink} from '@fortawesome/free-solid-svg-icons/faLink';
import {faUnlink} from '@fortawesome/free-solid-svg-icons/faUnlink';
import {library} from '@fortawesome/fontawesome-svg-core';
import * as _ from 'lodash';
import {filter} from 'rxjs/operators';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {STORAGE_ROOT_NODE} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';


library.add(faLink, faUnlink);

@Component({
  selector: 'lib-link-selection-button',
  templateUrl: './link-selection-button.component.html',
  styleUrls: ['./link-selection-button.component.css']
})
export class LinkSelectionButtonComponent implements OnDestroy {

  readonly linkSelectionIcon = new IconDynamic(
    new IconFa(faLink),
    {
      unlink: new IconFa(faUnlink)
    }
  );

  public link: BehaviorSubject<boolean>;

  private subscriptions: Subscription[] = [];

  constructor(@Inject(STORAGE_ID) private id: string,
              @Inject(STORAGE_ROOT_NODE) private rootNode: StorageNode,
              private treeControl: StorageTreeControlService,
              private eventBus: EventBusService,
              private localStorage: LocalStorageService) {
    const linkId = this.id + 'link-selection';
    this.link = new BehaviorSubject<boolean>(this.localStorage.getBoolean(linkId, true));
    this.subscriptions.push(this.link.subscribe((value) => this.localStorage.set(linkId, value)));
    this.subscriptions.push(this.eventBus.of<SelectNodeEvent>(SelectNodeEvent.CHANNEL)
      .pipe(filter((event) => {
        return this.link.value && event.node && event.node.path.startsWith(rootNode.path);
      }))
      .subscribe((event) => this.treeControl.selectOne(event.node)));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  switchLink() {
    this.link.next(!this.link.value);
  }

}
