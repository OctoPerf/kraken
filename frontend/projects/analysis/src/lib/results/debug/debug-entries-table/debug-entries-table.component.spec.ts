import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import {DebugEntriesTableComponent} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.component';
import {DebugEntriesTableService} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service';
import {
  debugEntriesTableServiceSpy,
  testDebugEntry
} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service.spec';
import {resultsTableServiceSpy} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import SpyObj = jasmine.SpyObj;
import {InjectDialogsModule} from 'projects/dialog/src/lib/inject-dialogs/inject-dialogs.module';
import {InjectDialogService} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service';
import {injectDialogServiceSpy} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service.spec';

describe('DebugEntriesTableComponent', () => {
  let component: DebugEntriesTableComponent;
  let fixture: ComponentFixture<DebugEntriesTableComponent>;
  let debugResult: SpyObj<DebugEntriesTableService>;
  let storage: SpyObj<StorageService>;

  beforeEach(waitForAsync(() => {
    debugResult = debugEntriesTableServiceSpy();
    storage = storageServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [DebugEntriesTableComponent],
      providers: [
        {provide: DebugEntriesTableService, useValue: debugResult},
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: ResultsTableService, useValue: resultsTableServiceSpy()},
        NodeEventToNodePipe,
      ]
    })
      .overrideProvider(StorageListService, {useValue: storageListServiceSpy()})
      .overrideProvider(DebugEntriesTableService, {useValue: debugResult})
      .overrideProvider(InjectDialogService, {useValue: injectDialogServiceSpy()})
      .overrideTemplate(DebugEntriesTableComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugEntriesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init datasource', () => {
    debugResult.valuesSubject.next([testDebugEntry()]);
    expect(component.dataSource).toBeTruthy();
  });

  it('should compareSelection', () => {
    component.compareSelection();
    expect(debugResult.compare).toHaveBeenCalled();
  });
});
