import {Component, Inject, ViewChild} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {ContextualMenuComponent} from 'projects/tree/src/lib/contextual-menu/contextual-menu.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {ContextualMenuEvent} from 'projects/storage/src/lib/events/contextual-menu-event';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'lib-storage-contextual-menu',
  templateUrl: './storage-contextual-menu.component.html',
  styleUrls: ['./storage-contextual-menu.component.scss']
})
export class StorageContextualMenuComponent {

  @ViewChild(ContextualMenuComponent, { static: true }) _contextualMenu: ContextualMenuComponent;

  constructor(
    @Inject(STORAGE_ID) private id: string,
    public treeControl: StorageTreeControlService,
    public copyPaste: CopyPasteService,
    private eventBus: EventBusService) {
    eventBus.of<ContextualMenuEvent>(ContextualMenuEvent.CHANNEL).pipe(filter(event => event.storageId === id)).subscribe((event) => {
      this._contextualMenu.open(event.$event);
    });
  }
}
