import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {CompareDialogComponent, CompareDialogData} from './compare-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {testResult} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {DebugEntryToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-string.pipe';
import {debugEntryToStringPipeSpy} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-string.pipe.spec';
import {testDebugEntry} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service.spec';

describe('CompareDialogComponent', () => {
  let component: CompareDialogComponent;
  let fixture: ComponentFixture<CompareDialogComponent>;
  let toString: SpyObj<DebugEntryToStringPipe>;
  let data: CompareDialogData;
  let storage: SpyObj<StorageService>;

  beforeEach(async(() => {
    data = {
      left: null,
      right: null,
      results: [testResult()]
    };

    toString = debugEntryToStringPipeSpy();
    toString.transform.and.returnValue(of('entry'));

    storage = storageServiceSpy();
    storage.find.and.returnValue(of([]));
    storage.listJSON.and.returnValue(of([testDebugEntry()]));

    TestBed.configureTestingModule({
      declarations: [CompareDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()},
        {provide: MAT_DIALOG_DATA, useValue: {}},
        {provide: DebugEntryToStringPipe, useValue: toString},
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
      ]
    })
      .overrideTemplate(CompareDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompareDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.debugEntries.length).toBe(1);
    expect(component.loading).toBeFalsy();
  });

  it('should select left', () => {
    spyOn(component, '_refresh');
    const entry = testDebugEntry();
    component.selectLeft(entry);
    expect(component._leftDebugEntry).toBe(entry);
  });

  it('should select right', () => {
    spyOn(component, '_refresh');
    const entry = testDebugEntry();
    component.selectRight(entry);
    expect(component._rightDebugEntry).toBe(entry);
  });

  it('should refresh', () => {
    const left = spyOn(component._leftDebugEntryEmitter, 'emit');
    const right = spyOn(component._rightDebugEntryEmitter, 'emit');
    component._leftDebugEntry = null;
    component._rightDebugEntry = null;
    component.loadingDiff = false;
    component._refresh();
    expect(component.loadingDiff).toBeTruthy();
    expect(left).not.toHaveBeenCalled();
    component._leftDebugEntry = testDebugEntry();
    component._refresh();
    expect(left).not.toHaveBeenCalled();
    component._rightDebugEntry = testDebugEntry();
    component._refresh();
    expect(left).toHaveBeenCalledWith(component._leftDebugEntry);
    expect(right).toHaveBeenCalledWith(component._rightDebugEntry);
  });

  it('should update left & right', fakeAsync(() => {
    component.loadingDiff = true;
    component._leftDebugEntryEmitter.emit(testDebugEntry());
    component._rightDebugEntryEmitter.emit(testDebugEntry());
    tick(1000);
    expect(component.left).toBe('entry');
    expect(component.right).toBe('entry');
    expect(component.loadingDiff).toBeFalsy();
  }));

});


