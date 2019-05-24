import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {testDebugChunk} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';

describe('DebugChunkToPathPipe', () => {
  it('create an instance', () => {
    const pipe = new DebugChunkToPathPipe(analysisConfigurationServiceSpy());
    expect(pipe).toBeTruthy();
    expect(pipe.transform(testDebugChunk())).toBe('gatling/results/debug-uuid/debug');
  });
});
