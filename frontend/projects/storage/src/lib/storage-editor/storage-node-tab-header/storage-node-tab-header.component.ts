import {Component, EventEmitter, Input, Output} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CloseTabsEvent} from 'projects/tabs/src/lib/close-tabs-event';
import {CLOSE_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'lib-storage-node-tab-header',
  templateUrl: './storage-node-tab-header.component.html',
  styleUrls: ['./storage-node-tab-header.component.scss']
})
export class StorageNodeTabHeaderComponent {

  readonly closeIcon = CLOSE_ICON;

  @Input() node: StorageNode;
  @Input() selected: boolean;
  @Input() pendingSave: boolean;
  @Output() closeNode = new EventEmitter<void>();
  @Output() openContextualMenu = new EventEmitter<MouseEvent>();

  constructor(private eventBus: EventBusService) {
  }

  expand() {
    this.eventBus.publish(new CloseTabsEvent());
  }
}
