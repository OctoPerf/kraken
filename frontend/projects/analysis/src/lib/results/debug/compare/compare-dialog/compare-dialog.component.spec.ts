import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {CompareDialogComponent, CompareDialogData} from './compare-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {testDebugChunk} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {testResult} from 'projects/analysis/src/lib/results/results-list.service.spec';
import {DebugChunkToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-string.pipe';
import {debugChunkToStringPipeSpy} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-string.pipe.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';

describe('CompareDialogComponent', () => {
  let component: CompareDialogComponent;
  let fixture: ComponentFixture<CompareDialogComponent>;
  let toString: SpyObj<DebugChunkToStringPipe>;
  let data: CompareDialogData;
  let storage: SpyObj<StorageService>;

  beforeEach(async(() => {
    data = {
      left: null,
      right: null,
      results: [testResult()]
    };

    toString = debugChunkToStringPipeSpy();
    toString.transform.and.returnValue(of('chunk'));

    storage = storageServiceSpy();
    storage.find.and.returnValue(of([]));
    storage.listJSON.and.returnValue(of([testDebugChunk()]));

    TestBed.configureTestingModule({
      declarations: [CompareDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()},
        {provide: MAT_DIALOG_DATA, useValue: {}},
        {provide: DebugChunkToStringPipe, useValue: toString},
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
    expect(component.debugChunks.length).toBe(1);
    expect(component.loading).toBeFalsy();
  });

  it('should select left', () => {
    spyOn(component, '_refresh');
    const chunk = testDebugChunk();
    component.selectLeft(chunk);
    expect(component._leftDebugChunk).toBe(chunk);
  });

  it('should select right', () => {
    spyOn(component, '_refresh');
    const chunk = testDebugChunk();
    component.selectRight(chunk);
    expect(component._rightDebugChunk).toBe(chunk);
  });

  it('should refresh', () => {
    const left = spyOn(component._leftDebugChunkEmitter, 'emit');
    const right = spyOn(component._rightDebugChunkEmitter, 'emit');
    component._leftDebugChunk = null;
    component._rightDebugChunk = null;
    component.loadingDiff = false;
    component._refresh();
    expect(component.loadingDiff).toBeTruthy();
    expect(left).not.toHaveBeenCalled();
    component._leftDebugChunk = testDebugChunk();
    component._refresh();
    expect(left).not.toHaveBeenCalled();
    component._rightDebugChunk = testDebugChunk();
    component._refresh();
    expect(left).toHaveBeenCalledWith(component._leftDebugChunk);
    expect(right).toHaveBeenCalledWith(component._rightDebugChunk);
  });

  it('should update left & right', fakeAsync(() => {
    component.loadingDiff = true;
    component._leftDebugChunkEmitter.emit(testDebugChunk());
    component._rightDebugChunkEmitter.emit(testDebugChunk());
    tick(1000);
    expect(component.left).toBe('chunk');
    expect(component.right).toBe('chunk');
    expect(component.loadingDiff).toBeFalsy();
  }));

});


