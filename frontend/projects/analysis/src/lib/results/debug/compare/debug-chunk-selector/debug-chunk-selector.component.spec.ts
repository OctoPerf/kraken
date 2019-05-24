import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DebugChunkSelectorComponent} from './debug-chunk-selector.component';
import {testDebugChunk} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {testResult, testResultDebug} from 'projects/analysis/src/lib/results/results-list.service.spec';

describe('DebugChunkSelectorComponent', () => {
  let component: DebugChunkSelectorComponent;
  let fixture: ComponentFixture<DebugChunkSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DebugChunkSelectorComponent]
    })
      .overrideTemplate(DebugChunkSelectorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugChunkSelectorComponent);
    component = fixture.componentInstance;
    component.debugChunks = [testDebugChunk()];
    component.results = [testResult(), testResultDebug()];
    component.debugChunkId = testDebugChunk().id;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter', () => {
    expect(component.filter(testDebugChunk().resultId).length).toBe(1);
  });

  it('should handle selection', () => {
    const emit = spyOn(component.debugSelected, 'emit');
    component.debugChunkSelected(testDebugChunk().resultId);
    expect(emit).toHaveBeenCalled();
  });
});
