import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ResultsTableComponent} from 'projects/analysis/src/lib/results/results-table/results-table.component';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-table/gatling-result.service';
import {gatlingResultServiceSpy} from 'projects/analysis/src/lib/results/results-table/gatling-result.service.spec';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {
  resultsTableServiceSpy,
  testResult
} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {OpenGatlingReportsDialogComponent} from 'projects/analysis/src/lib/analysis-dialogs/open-gatling-reports-dialog/open-gatling-reports-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {of} from 'rxjs';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {defaultDialogServiceSpy} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service.spec';
import SpyObj = jasmine.SpyObj;

describe('ResultsTableComponent', () => {
  let component: ResultsTableComponent;
  let fixture: ComponentFixture<ResultsTableComponent>;
  let results: SpyObj<ResultsTableService>;
  let gatling: SpyObj<GatlingResultService>;
  let dialogs: SpyObj<DefaultDialogService>;

  beforeEach(waitForAsync(() => {
    results = resultsTableServiceSpy();
    gatling = gatlingResultServiceSpy();
    dialogs = defaultDialogServiceSpy();

    TestBed.configureTestingModule({
      declarations: [ResultsTableComponent],
      providers: [
        {provide: GatlingResultService, useValue: gatling},
        {provide: ResultsTableService, useValue: results},
        {provide: DefaultDialogService, useValue: dialogs}
      ]
    })
      .overrideProvider(GatlingResultService, {useValue: gatling})
      .overrideTemplate(ResultsTableComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(results.init).toHaveBeenCalled();
  });

  it('should init datasource', () => {
    results.valuesSubject.next([testResult()]);
    expect(component.dataSource).toBeTruthy();
  });

  it('should open menu', () => {
    const event: any = {};
    component.menu = jasmine.createSpyObj('menu', ['open']);
    component.openMenu(event);
    expect(component.menu.open).toHaveBeenCalledWith(event);
  });

  it('should open gatling reports dialog', () => {
    const result = testResult();
    component.openGatlingReportsDialog(result);
    expect(dialogs.open).toHaveBeenCalledWith(OpenGatlingReportsDialogComponent, DialogSize.SIZE_SM, {result});
  });

  it('should open grafana reports selection', () => {
    gatling.canOpenGrafanaReport.and.returnValue(true);
    expect(component.openGrafanaSelection()).toBe(true);
    expect(gatling.canOpenGrafanaReport).toHaveBeenCalledTimes(1);
    expect(gatling.openGrafanaReport).toHaveBeenCalledTimes(1);
  });

  it('should not open grafana reports selection', () => {
    gatling.canOpenGrafanaReport.and.returnValue(false);
    expect(component.openGrafanaSelection()).toBe(false);
    expect(gatling.canOpenGrafanaReport).toHaveBeenCalledTimes(1);
    expect(gatling.openGrafanaReport).toHaveBeenCalledTimes(0);
  });

  it('should delete result selection and force', () => {
    gatling.canDeleteResult.and.returnValue(true);
    gatling.deleteResult.and.returnValue(of('ok'));
    expect(component.deleteSelection(true)).toBe(true);
    expect(gatling.canDeleteResult).toHaveBeenCalledTimes(1);
    expect(gatling.deleteResult).toHaveBeenCalledTimes(1);
  });

  it('should not delete result selection and force', () => {
    gatling.canDeleteResult.and.returnValue(false);
    expect(component.deleteSelection(true)).toBe(false);
    expect(gatling.canDeleteResult).toHaveBeenCalledTimes(1);
    expect(gatling.deleteResult).toHaveBeenCalledTimes(0);
  });

  it('should not delete result selection', () => {
    gatling.canDeleteResult.and.returnValue(false);
    expect(component.deleteSelection(false)).toBe(false);
    expect(gatling.canDeleteResult).toHaveBeenCalledTimes(1);
    expect(gatling.deleteResult).toHaveBeenCalledTimes(0);
  });
});
