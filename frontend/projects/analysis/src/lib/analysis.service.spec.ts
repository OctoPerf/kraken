import {TestBed} from '@angular/core/testing';

import {AnalysisService} from './analysis.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';

export const analysisServiceSpy = () => {
  const spy = jasmine.createSpyObj('AnalysisService', [
    'deleteTest',
  ]);
  return spy;
};

describe('AnalysisService', () => {
  let service: AnalysisService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        AnalysisService,
      ]
    });
    service = TestBed.inject(AnalysisService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete test', () => {
    const resultId = 'resultId';
    service.deleteTest(resultId).subscribe(value => expect(value).toBe(resultId), () => fail('delete failed'));
    const req = httpTestingController.expectOne(request => request.url === 'analysisApiUrl/result');
    expect(req.request.method).toBe('DELETE');
    expect(req.request.params.get('resultId')).toEqual('resultId');
    req.flush(resultId);
  });

});
