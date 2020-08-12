import {Inject, Injectable, OnDestroy} from '@angular/core';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeScrollService} from 'projects/storage/src/lib/storage-tree/storage-tree-scroll.service';

@Injectable()
export class StorageKeyBindingService implements OnDestroy {

  private keyBindings: KeyBinding[] = [];

  constructor(
    private scroll: StorageTreeScrollService,
    public treeControl: StorageTreeControlService,
    @Inject(STORAGE_ID) public id: string,
    @Inject(STORAGE_ROOT_NODE) private readonly rootNode: StorageNode,
    private keys: KeyBindingsService,
    private dataSource: StorageTreeDataSourceService,
    public storage: StorageService) {
  }

  init() {
    this.keyBindings.push(new KeyBinding(['ArrowUp', 'Up'], this.upSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['ArrowDown', 'Down'], this.downSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ArrowUp', 'shift + Up'], this.upMultiSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ArrowDown', 'shift + Down'], this.downMultiSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Enter'], this.openSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Right', 'ArrowRight'], this.rightSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Left', 'ArrowLeft'], this.leftSelection.bind(this), this.id));
    this.keyBindings.forEach(binding => {
      this.keys.add([binding]);
    });
  }

  ngOnDestroy() {
    this.keyBindings.forEach(binding => this.keys.remove([binding]));
  }

  public upSelection(): boolean {
    const nodeToSelect = this.selectNextOpen(index => index - 1);
    if (nodeToSelect) {
      this.treeControl.selectOne(nodeToSelect);
      this.scroll.updateScroll();
      return true;
    }
    return false;
  }

  public upMultiSelection(): boolean {
    const nodeToSelect = this.selectNextOpen(index => index - 1);
    if (nodeToSelect) {
      if (this.treeControl.isSelected(nodeToSelect)) {
        this.treeControl.deselectNode(this.data[this.lastIndexSelection], nodeToSelect);
      } else {
        this.treeControl.selectNode(nodeToSelect);
      }
      this.scroll.updateScroll();
      return true;
    }
    return false;
  }

  public downSelection(): boolean {
    const nodeToSelect = this.selectNextOpen(index => index + 1);
    if (nodeToSelect) {
      this.treeControl.selectOne(nodeToSelect);
      this.scroll.updateScroll();
      return true;
    }
    return false;
  }

  public downMultiSelection(): boolean {
    const nextNode = this.selectNextOpen(index => index + 1);
    if (nextNode) {
      if (this.treeControl.isSelected(nextNode)) {
        this.treeControl.deselectNode(this.data[this.lastIndexSelection], nextNode);
      } else {
        this.treeControl.selectNode(nextNode);
      }
      this.scroll.updateScroll();
      return true;
    }
    return false;
  }

  private selectNextOpen(getNextIndex: (index: number) => number): StorageNode {
    const lastIndex = this.lastIndexSelection;
    const newIndex = getNextIndex(lastIndex);
    if (newIndex >= 0 && newIndex < this.data.length) {
      return this.data[getNextIndex(lastIndex)];
    }
    return null;
  }

  private get lastIndexSelection(): number {
    return this.dataSource.indexOf(this.treeControl._lastSelection);
  }

  private get data(): StorageNode[] {
    return this.dataSource._expandedNodes;
  }

  public openSelection(): boolean {
    this.treeControl.selected.forEach(selectedNode => {
      this.treeControl.nodeDoubleClick(selectedNode);
    });
    return true;
  }

  public rightSelection(): boolean {
    const node = this.treeControl._lastSelection;
    if (node.type === 'DIRECTORY' && !this.treeControl.isExpanded(node)) {
      this.treeControl.expand(node);
    } else {
      return this.downSelection();
    }
    return true;
  }

  public leftSelection(): boolean {
    const node = this.treeControl._lastSelection;
    if (node.type !== 'DIRECTORY' || (node.type === 'DIRECTORY' && !this.treeControl.isExpanded(node))) {
      const parent = this.dataSource.parentNode(node);
      if (parent.path !== this.rootNode.path) {
        this.treeControl.selectOne(parent);
      } else {
        return this.upSelection();
      }
    } else {
      this.treeControl.collapse(node);
    }
    this.scroll.updateScroll();
    return true;
  }

  public deleteSelection(force = false): boolean {
    const nodes = this.treeControl.selected;
    this.storage.deleteFiles(nodes, force);
    return true;
  }

}
