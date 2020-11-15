import {AfterViewInit, Component, Inject, InjectionToken, OnInit, Optional, ViewChild} from '@angular/core';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faEllipsisV} from '@fortawesome/free-solid-svg-icons/faEllipsisV';
import {library} from '@fortawesome/fontawesome-svg-core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {ComponentPortal} from '@angular/cdk/portal';
import {StorageContextualMenuComponent} from 'projects/storage/src/lib/storage-menu/storage-contextual-menu/storage-contextual-menu.component';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {StorageKeyBindingService} from 'projects/storage/src/lib/storage-tree/storage-key-binding.service';
import {faCompress} from '@fortawesome/free-solid-svg-icons/faCompress';
import {CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {StorageTreeScrollService} from 'projects/storage/src/lib/storage-tree/storage-tree-scroll.service';

library.add(
  faEllipsisV,
  faCompress
);

export const STORAGE_CONTEXTUAL_MENU = new InjectionToken<any /*ComponentType<any>*/>('StorageContextualMenu');
export const STORAGE_TREE_LABEL = new InjectionToken<string>('StorageTreeLabel');

@Component({
  selector: 'lib-storage-tree',
  templateUrl: './storage-tree.component.html',
  styleUrls: ['./storage-tree.component.scss'],
  providers: [
    StorageListService,
    StorageTreeDataSourceService,
    StorageTreeControlService,
    StorageKeyBindingService,
    StorageTreeScrollService,
    CopyPasteService,
  ]
})
export class StorageTreeComponent implements OnInit, AfterViewInit {

  // Icons
  readonly menuIcon = new IconFa(faEllipsisV, 'primary');
  readonly collapseAllIcon = new IconFa(faCompress, 'foreground');

  public contextualMenu: ComponentPortal<any>;
  public label: string;

  @ViewChild('scrollableTree') scrollableTree: CdkVirtualScrollViewport;

  constructor(public treeControl: StorageTreeControlService,
              public dataSource: StorageTreeDataSourceService,
              public copyPaste: CopyPasteService,
              @Inject(STORAGE_CONTEXTUAL_MENU) @Optional() contextualMenuType: any /*ComponentType<any>*/,
              @Inject(STORAGE_TREE_LABEL) @Optional() label: string,
              @Inject(STORAGE_ID) public id: string,
              @Inject(STORAGE_ROOT_NODE) private readonly rootNode: StorageNode,
              private eventBus: EventBusService,
              private keyBinding: StorageKeyBindingService, // DO NOT REMOVE
              private scroll: StorageTreeScrollService) {
    dataSource.treeControl = treeControl;
    this.contextualMenu = new ComponentPortal<any>(contextualMenuType ? contextualMenuType : StorageContextualMenuComponent);
    this.label = label ? label : 'Files';
  }

  ngOnInit() {
    this.dataSource.init();
  }

  ngAfterViewInit(): void {
    this.scroll.init(this.scrollableTree);
    this.keyBinding.init();
  }

  selectHelpPage() {
    this.eventBus.publish(new SelectHelpEvent(this.id as HelpPageId));
  }

  depth(node: StorageNode): number {
    return node.depth - this.rootNode.depth - 1;
  }
}
