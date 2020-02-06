import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ResultsTableComponent} from 'projects/analysis/src/lib/results/results-table/results-table.component';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-table/gatling-result.service';
import {gatlingResultServiceSpy} from 'projects/analysis/src/lib/results/results-table/gatling-result.service.spec';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {
  resultsTableServiceSpy,
  testResult
} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import SpyObj = jasmine.SpyObj;
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {OpenGatlingReportsDialogComponent} from 'projects/analysis/src/lib/analysis-dialogs/open-gatling-reports-dialog/open-gatling-reports-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

describe('ResultsListComponent', () => {
  let component: ResultsTableComponent;
  let fixture: ComponentFixture<ResultsTableComponent>;
  let results: SpyObj<ResultsTableService>;
  let gatling: SpyObj<GatlingResultService>;
  let dialogs: SpyObj<DialogService>;

  beforeEach(async(() => {
    results = resultsTableServiceSpy();
    gatling = gatlingResultServiceSpy();
    dialogs = dialogsServiceSpy();

    TestBed.configureTestingModule({
      declarations: [ResultsTableComponent],
      providers: [
        {provide: GatlingResultService, useValue: gatling},
        {provide: ResultsTableService, useValue: results},
        {provide: DialogService, useValue: dialogs}
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
});
