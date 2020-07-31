import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import { OnDestroy, OnInit, Directive } from '@angular/core';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';

@Directive()
export class KeyBoundMenuItem implements OnInit, OnDestroy {

  constructor(protected treeControl: StorageTreeControlService,
              protected keys: KeyBindingsService,
              public binding: KeyBinding) {
  }

  ngOnInit() {
    this.keys.add([this.binding]);
  }

  ngOnDestroy() {
    this.keys.remove([this.binding]);
  }

  _handleKey(operation: (nodes: StorageNode[]) => void): boolean {
    if (this.treeControl.hasSelection) {
      operation(this.treeControl.selected);
      return true;
    }
    return false;
  }

}
