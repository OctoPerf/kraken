import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

export const PATH_REGEXP = /([0-9a-z\-]*)\/debug\/(.*?).debug$/;

@Pipe({
  name: 'isDebugEntryStorageNode'
})
export class IsDebugEntryStorageNodePipe implements PipeTransform {

  constructor(private analysisConfiguration: AnalysisConfigurationService) {
  }

  transform(node: StorageNode): boolean {
    const rootDepth = this.analysisConfiguration.analysisRootNode.depth + 3;
    return node
      && node.depth === rootDepth
      && node.path.startsWith(this.analysisConfiguration.analysisRootNode.path)
      && !!node.path.match(PATH_REGEXP);
  }

}
