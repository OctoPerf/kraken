import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DebugEntrySelectorComponent} from 'projects/analysis/src/lib/results/debug/compare/debug-entry-selector/debug-entry-selector.component';
import {testResult, testResultDebug} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {testDebugEntry} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service.spec';

describe('DebugEntrySelectorComponent', () => {
  let component: DebugEntrySelectorComponent;
  let fixture: ComponentFixture<DebugEntrySelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DebugEntrySelectorComponent]
    })
      .overrideTemplate(DebugEntrySelectorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugEntrySelectorComponent);
    component = fixture.componentInstance;
    component.debugEntries = [testDebugEntry()];
    component.results = [testResult(), testResultDebug()];
    component.debugEntryId = testDebugEntry().id;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter', () => {
    expect(component.filter(testDebugEntry().resultId).length).toBe(1);
  });

  it('should handle selection', () => {
    const emit = spyOn(component.debugSelected, 'emit');
    component.debugEntrySelected(testDebugEntry().resultId);
    expect(emit).toHaveBeenCalled();
  });
});
