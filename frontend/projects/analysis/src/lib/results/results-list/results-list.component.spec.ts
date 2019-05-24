import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ResultsListComponent} from 'projects/analysis/src/lib/results/results-list/results-list.component';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-list/gatling-result.service';
import {gatlingResultServiceSpy} from 'projects/analysis/src/lib/results/results-list/gatling-result.service.spec';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {resultsListServiceSpy, testResult} from 'projects/analysis/src/lib/results/results-list.service.spec';
import SpyObj = jasmine.SpyObj;

describe('ResultsListComponent', () => {
  let component: ResultsListComponent;
  let fixture: ComponentFixture<ResultsListComponent>;
  let results: SpyObj<ResultsListService>;
  let gatling: SpyObj<GatlingResultService>;

  beforeEach(async(() => {
    results = resultsListServiceSpy();
    gatling = gatlingResultServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ResultsListComponent],
      providers: [
        {provide: GatlingResultService, useValue: gatling},
        {provide: ResultsListService, useValue: results}
      ]
    })
      .overrideProvider(GatlingResultService, {useValue: gatling})
      .overrideTemplate(ResultsListComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultsListComponent);
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
