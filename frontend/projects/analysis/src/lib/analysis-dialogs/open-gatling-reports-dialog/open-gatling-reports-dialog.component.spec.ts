import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {OpenGatlingReportsDialogComponent} from './open-gatling-reports-dialog.component';
import {of, throwError} from 'rxjs';
import {testResult, testResultNode} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import SpyObj = jasmine.SpyObj;
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('OpenGatlingReportsDialogComponent', () => {
  let component: OpenGatlingReportsDialogComponent;
  let fixture: ComponentFixture<OpenGatlingReportsDialogComponent>;
  let storage: SpyObj<StorageService>;
  let analysisConfiguration: SpyObj<AnalysisConfigurationService>;
  let window: SpyObj<WindowService>;

  beforeEach(async(() => {
    storage = storageServiceSpy();
    analysisConfiguration = analysisConfigurationServiceSpy();
    window = windowSpy();

    TestBed.configureTestingModule({
      declarations: [OpenGatlingReportsDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {result: testResult()}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfiguration},
        {provide: WindowService, useValue: window},
      ]
    })
      .overrideTemplate(OpenGatlingReportsDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenGatlingReportsDialogComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.loading).toBe(true);
  });

  it('should create list reports', () => {
    storage.find.and.returnValue(of([testResultNode()]));
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.loading).toBe(false);
    expect(component.reports.length).toBe(1);
  });

  it('should create fail to list reports', () => {
    storage.find.and.returnValue(throwError('Fail!'));
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.loading).toBe(false);
    expect(component.reports.length).toBe(0);
  });

  it('should openGatlingReport', () => {
    const node = testStorageFileNode();
    window.open.and.callFake(url => url.subscribe(val => expect(val).toBe('staticApiUrl/spotbugs/main.html'), err => fail(err)));
    component.openGatlingReport(node);
    expect(window.open).toHaveBeenCalled();
  });
});
