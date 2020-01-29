import {
  AfterViewInit,
  Component,
  ElementRef,
  Inject,
  InjectionToken,
  OnInit,
  Optional,
  QueryList,
  ViewChild,
  ViewChildren
} from '@angular/core';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faEllipsisV} from '@fortawesome/free-solid-svg-icons/faEllipsisV';
import {library} from '@fortawesome/fontawesome-svg-core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {faChevronDown} from '@fortawesome/free-solid-svg-icons/faChevronDown';
import {faMinus} from '@fortawesome/free-solid-svg-icons/faMinus';
import {ComponentPortal} from '@angular/cdk/portal';
import {StorageContextualMenuComponent} from 'projects/storage/src/lib/storage-menu/storage-contextual-menu/storage-contextual-menu.component';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {StorageKeyBindingService} from 'projects/storage/src/lib/storage-tree/storage-key-binding.service';
import {ScrollPositionComponent} from 'projects/storage/src/lib/storage-tree/scroll-position/scroll-position.component';
import {StorageNodeComponent} from 'projects/storage/src/lib/storage-tree/storage-node/storage-node.component';

library.add(
  faEllipsisV,
  faChevronDown,
  faMinus
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
    CopyPasteService,
    ScrollPositionComponent,
  ]
})
export class StorageTreeComponent implements OnInit, AfterViewInit {

  // Icons
  readonly menuIcon = new IconFa(faEllipsisV, 'primary');
  readonly expandAllIcon = new IconFaAddon(
    new IconFa(faChevronDown, 'foreground', 'down-4'),
    new IconFa(faMinus, 'foreground', 'up-6'),
  );

  public contextualMenu: ComponentPortal<any>;
  public label: string;

  @ViewChild('scrollableTree', {static: false}) scrollableTree: ElementRef<HTMLElement>;
  @ViewChildren(StorageNodeComponent) treeNodes: QueryList<StorageNodeComponent>;

  constructor(public treeControl: StorageTreeControlService,
              public dataSource: StorageTreeDataSourceService,
              public copyPaste: CopyPasteService,
              @Inject(STORAGE_CONTEXTUAL_MENU) @Optional() contextualMenuType: any /*ComponentType<any>*/,
              @Inject(STORAGE_TREE_LABEL) @Optional() label: string,
              @Inject(STORAGE_ID) public id: string,
              private eventBus: EventBusService,
              private keyBinding: StorageKeyBindingService) {
    dataSource.treeControl = treeControl;
    this.contextualMenu = new ComponentPortal<any>(contextualMenuType ? contextualMenuType : StorageContextualMenuComponent);
    this.label = label ? label : 'Files';
  }

  ngOnInit() {
    this.dataSource.init();
  }

  ngAfterViewInit(): void {
    this.keyBinding.init(this.treeNodes, this.scrollableTree);
  }

  selectHelpPage() {
    this.eventBus.publish(new SelectHelpEvent(this.id as HelpPageId));
  }

  hasChild = (_number: number, _nodeData: StorageNode) => _nodeData.type === 'DIRECTORY';

}
