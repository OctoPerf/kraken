import {
  Component,
  ElementRef,
  Inject,
  InjectionToken,
  Injector,
  Input,
  OnDestroy,
  OnInit,
  Optional
} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {ComponentPortal, ComponentType, PortalInjector} from '@angular/cdk/portal';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNodeButtonsComponent} from 'projects/storage/src/lib/storage-menu/storage-node-buttons/storage-node-buttons.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {GitFileStatusEvent} from 'projects/git/src/lib/events/git-file-status-event';
import {filter, map} from 'rxjs/operators';
import {Color} from 'projects/color/src/lib/color';
import {Subscription} from 'rxjs';
import {GitFileStatusService} from 'projects/git/src/lib/git-file-status/git-file-status.service';

export const STORAGE_NODE_BUTTONS = new InjectionToken<ComponentType<any>>('StorageNodeButtons');

@Component({
  selector: 'lib-storage-node',
  templateUrl: './storage-node.component.html',
  styleUrls: ['./storage-node.component.scss']
})
export class StorageNodeComponent implements OnDestroy {

  public hasChild: boolean;
  public nodeButtons: ComponentPortal<any>;
  public color: Color = 'foreground';

  @Input() public expanded: boolean;
  private _hover = false;
  private _node: StorageNode;

  _subscription: Subscription;

  constructor(public ref: ElementRef,
              public treeControl: StorageTreeControlService,
              private injector: Injector,
              @Inject(STORAGE_NODE_BUTTONS) @Optional() private nodeButtonsType: any /*ComponentType<any>*/,
              private eventBus: EventBusService,
              private gitFileStatus: GitFileStatusService) {
    this._subscription = this.eventBus.of<GitFileStatusEvent>(GitFileStatusEvent.CHANNEL)
      .pipe(filter(event => event.path === this._node.path),
        map(event => event.color))
      .subscribe(color => {
        this.color = color;
      });
  }

  ngOnDestroy(): void {
    this._subscription.unsubscribe();
  }

  @Input() set node(node: StorageNode) {
    this._node = node;
    this.hasChild = node.type === 'DIRECTORY';
    this.hover = false;
    this.color = this.gitFileStatus.getEvent(this._node.path).color;
  }

  get node(): StorageNode {
    return this._node;
  }

  set hover(hover: boolean) {
    if (hover) {
      this.nodeButtons = new ComponentPortal(this.nodeButtonsType ? this.nodeButtonsType : StorageNodeButtonsComponent, null,
        new PortalInjector(this.injector, new WeakMap([[STORAGE_NODE, this.node]])));
    } else if (this.nodeButtons && this.nodeButtons.isAttached) {
      this.nodeButtons.detach();
    }
    this._hover = hover;
  }

  get hover(): boolean {
    return this._hover;
  }
}
