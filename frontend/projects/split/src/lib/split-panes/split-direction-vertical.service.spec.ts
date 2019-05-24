import {inject, TestBed} from '@angular/core/testing';
import {SplitDirectionVerticalService} from 'projects/split/src/lib/split-panes/split-direction-vertical.service';

describe('SplitDirectionVerticalService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SplitDirectionVerticalService]
    });
  });

  it('should be created', inject([SplitDirectionVerticalService], (service: SplitDirectionVerticalService) => {
    expect(service).toBeTruthy();
  }));

  it('should divToSize', inject([SplitDirectionVerticalService], (service: SplitDirectionVerticalService) => {
    expect(service.divToSize({clientHeight: 42})).toBe(42);
  }));

  it('should eventToSize', inject([SplitDirectionVerticalService], (service: SplitDirectionVerticalService) => {
    expect(service.eventToSize({clientY: 42} as MouseEvent)).toBe(42);
  }));
});
