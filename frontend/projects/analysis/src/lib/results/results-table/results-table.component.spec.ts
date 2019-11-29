import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ResultsTableComponent} from 'projects/analysis/src/lib/results/results-table/results-table.component';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-table/gatling-result.service';
import {gatlingResultServiceSpy} from 'projects/analysis/src/lib/results/results-table/gatling-result.service.spec';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import SpyObj = jasmine.SpyObj;
import {
  resultsTableServiceSpy,
  testResult
} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';

describe('ResultsListComponent', () => {
  let component: ResultsTableComponent;
  let fixture: ComponentFixture<ResultsTableComponent>;
  let results: SpyObj<ResultsTableService>;
  let gatling: SpyObj<GatlingResultService>;

  beforeEach(async(() => {
    results = resultsTableServiceSpy();
    gatling = gatlingResultServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ResultsTableComponent],
      providers: [
        {provide: GatlingResultService, useValue: gatling},
        {provide: ResultsTableService, useValue: results}
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
});
