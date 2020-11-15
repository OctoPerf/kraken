import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {OpenGatlingReportsDialogComponent} from './open-gatling-reports-dialog.component';
import {of, throwError} from 'rxjs';
import {testResult, testResultNode} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageStaticService} from 'projects/storage/src/lib/storage-static.service';
import {storageStaticServiceSpy} from 'projects/storage/src/lib/storage-static.service.spec';
import SpyObj = jasmine.SpyObj;

describe('OpenGatlingReportsDialogComponent', () => {
  let component: OpenGatlingReportsDialogComponent;
  let fixture: ComponentFixture<OpenGatlingReportsDialogComponent>;
  let storage: SpyObj<StorageService>;
  let analysisConfiguration: SpyObj<AnalysisConfigurationService>;
  let storageStatic: SpyObj<StorageStaticService>;

  beforeEach(waitForAsync(() => {
    storage = storageServiceSpy();
    analysisConfiguration = analysisConfigurationServiceSpy();
    storageStatic = storageStaticServiceSpy();

    TestBed.configureTestingModule({
      declarations: [OpenGatlingReportsDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {result: testResult()}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfiguration},
        {provide: StorageStaticService, useValue: storageStatic},
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
    component.openGatlingReport(node);
    expect(storageStatic.openStaticPage).toHaveBeenCalledWith('spotbugs/main.html');
  });
});
