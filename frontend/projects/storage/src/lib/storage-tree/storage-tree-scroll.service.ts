import {Injectable} from '@angular/core';
import {CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';

@Injectable()
export class StorageTreeScrollService {

  private scrollableTree: CdkVirtualScrollViewport;

  constructor(
    public treeControl: StorageTreeControlService,
    private dataSource: StorageTreeDataSourceService) {
  }

  public init(scrollableTree: CdkVirtualScrollViewport): void {
    this.scrollableTree = scrollableTree;
  }

  public updateScroll() {
    if (this.treeControl._lastSelection) {
      this.scrollableTree.scrollToIndex(this.dataSource.findIndex(this.treeControl._lastSelection), 'smooth');
    }
  }
}
