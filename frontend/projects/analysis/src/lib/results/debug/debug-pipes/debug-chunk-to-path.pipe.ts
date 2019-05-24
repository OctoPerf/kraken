import {Pipe, PipeTransform} from '@angular/core';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

@Pipe({
  name: 'debugChunkToPath',
})
export class DebugChunkToPathPipe implements PipeTransform {

  constructor(private analysisConfiguration: AnalysisConfigurationService) {
  }

  transform(chunk: DebugChunk): string {
    return `${this.analysisConfiguration.analysisRootNode.path}/${chunk.resultId}/debug`;
  }

}
