import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {testDebugEntry} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service.spec';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';

describe('DebugEntryToPathPipe', () => {
  it('create an instance', () => {
    const pipe = new DebugEntryToPathPipe(analysisConfigurationServiceSpy());
    expect(pipe).toBeTruthy();
    expect(pipe.transform(testDebugEntry())).toBe('gatling/results/debug-uuid/debug');
  });
});
