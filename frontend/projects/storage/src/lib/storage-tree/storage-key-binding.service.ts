import {ElementRef, Inject, Injectable, OnDestroy, QueryList} from '@angular/core';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageNodeComponent} from 'projects/storage/src/lib/storage-tree/storage-node/storage-node.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {GuiToolsService} from 'projects/tools/src/lib/gui-tools.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';

@Injectable()
export class StorageKeyBindingService implements OnDestroy {

  private keyBindings: KeyBinding[] = [];
  private treeNodes: QueryList<StorageNodeComponent>;
  private scrollableTree: ElementRef<HTMLElement>;


  constructor(
    public treeControl: StorageTreeControlService,
    @Inject(STORAGE_ID) public id: string,
    @Inject(STORAGE_ROOT_NODE) private readonly rootNode: StorageNode,
    private keys: KeyBindingsService,
    private dataSource: StorageTreeDataSourceService,
    public storage: StorageService,
    public guiTools: GuiToolsService,
    public simulationService: SimulationService) {
  }

  public init(matTreeNodes: QueryList<StorageNodeComponent>, scrollableTree: ElementRef<HTMLElement>): void {
    this.keyBindings.push(new KeyBinding(['ArrowUp', 'Up'], this.upSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['ArrowDown', 'Down'], this.downSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ArrowUp', 'shift + Up'], this.upMultiSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ArrowDown', 'shift + Down'], this.downMultiSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Enter'], this.openSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Right', 'ArrowRight'], this.rightSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['Left', 'ArrowLeft'], this.leftSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['F2'], this.renameSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ctrl + X'], this.runSelection.bind(this), this.id));
    this.keyBindings.push(new KeyBinding(['shift + ctrl + D'], this.debugSelection.bind(this), this.id));
    this.keyBindings.forEach(binding => {
      this.keys.add([binding]);
    });
    this.treeNodes = matTreeNodes;
    this.scrollableTree = scrollableTree;
  }

  ngOnDestroy() {
    this.keyBindings.forEach(binding => this.keys.remove([binding]));
  }

  private get selectedNode() {
    return this.treeNodes.filter(item => this.treeControl._lastSelection.path === item.node.path)[0];
  }

  private updateScroll() {
    this.guiTools.scrollTo(this.scrollableTree, () => this.selectedNode.ref.nativeElement);
  }

  public upSelection(): boolean {
    const nodeToSelect = this.selectNextOpen(index => index - 1);
    if (nodeToSelect) {
      this.treeControl.selectOne(nodeToSelect);
      this.updateScroll();
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
      this.updateScroll();
      return true;
    }
    return false;
  }

  public downSelection(): boolean {
    const nodeToSelect = this.selectNextOpen(index => index + 1);
    if (nodeToSelect) {
      this.treeControl.selectOne(nodeToSelect);
      this.updateScroll();
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
      this.updateScroll();
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
    return _.indexOf(this.dataSource._expandedNodes, this.treeControl._lastSelection);
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
    this.updateScroll();
    return true;
  }

  public deleteSelection(force = false): boolean {
    const nodes = this.treeControl.selected;
    this.storage.deleteFiles(nodes, force);
    return true;
  }

  public renameSelection(): boolean {
    const nodeSelected = this.treeControl._lastSelection;
    const parent = this.dataSource.parentNode(nodeSelected);
    this.storage.rename(nodeSelected, parent);
    return true;
  }

  public runSelection(): boolean {
    const nodeSelected = this.treeControl._lastSelection;
    this.simulationService.run(nodeSelected);
    return true;
  }

  public debugSelection(): boolean {
    const nodeSelected = this.treeControl._lastSelection;
    this.simulationService.debug(nodeSelected);
    return true;
  }
}
