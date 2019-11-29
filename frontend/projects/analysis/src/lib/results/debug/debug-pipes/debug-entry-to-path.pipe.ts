import {Pipe, PipeTransform} from '@angular/core';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

@Pipe({
  name: 'debugEntryToPath',
})
export class DebugEntryToPathPipe implements PipeTransform {

  constructor(private analysisConfiguration: AnalysisConfigurationService) {
  }

  transform(entry: DebugEntry): string {
    return `${this.analysisConfiguration.analysisRootNode.path}/${entry.resultId}/debug`;
  }

}
