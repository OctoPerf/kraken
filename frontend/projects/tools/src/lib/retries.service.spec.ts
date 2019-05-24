import {TestBed} from '@angular/core/testing';

import {RetriesService} from './retries.service';

export const retriesServiceSpy = () => {
  const retrySpy = jasmine.createSpyObj('Retry', ['getDelay', 'reset']);
  retrySpy.getDelay.and.returnValue(1000);
  const spy = jasmine.createSpyObj('RetriesService', ['get']);
  spy.get.and.returnValue(retrySpy);
  return spy;
};

describe('RetriesService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [RetriesService]
  }));

  it('should be created', () => {
    const service: RetriesService = TestBed.get(RetriesService);
    expect(service).toBeTruthy();
    const retry = service.get();
    expect(retry.getDelay()).toEqual(2000);
    expect(retry.getDelay()).toEqual(4000);
    retry.reset();
    expect(retry.getDelay()).toEqual(2000);
  });
});
