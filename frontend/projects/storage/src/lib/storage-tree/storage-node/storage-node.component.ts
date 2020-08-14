import {Component, ElementRef, Inject, InjectionToken, Injector, Input, OnInit, Optional} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {ComponentPortal, ComponentType, PortalInjector} from '@angular/cdk/portal';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNodeButtonsComponent} from 'projects/storage/src/lib/storage-menu/storage-node-buttons/storage-node-buttons.component';

export const STORAGE_NODE_BUTTONS = new InjectionToken<ComponentType<any>>('StorageNodeButtons');

@Component({
  selector: 'lib-storage-node',
  templateUrl: './storage-node.component.html',
  styleUrls: ['./storage-node.component.scss']
})
export class StorageNodeComponent {

  public hasChild: boolean;
  public nodeButtons: ComponentPortal<any>;

  @Input() public expanded: boolean;
  private _hover = false;
  private _node: StorageNode;

  constructor(public ref: ElementRef,
              public treeControl: StorageTreeControlService,
              private injector: Injector,
              @Inject(STORAGE_NODE_BUTTONS) @Optional() private nodeButtonsType: any /*ComponentType<any>*/) {
  }

  @Input() set node(node: StorageNode) {
    this._node = node;
    this.hasChild = node.type === 'DIRECTORY';
    this.hover = false;
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
