import { TestBed, inject } from '@angular/core/testing';
import {SplitDirectionHorizontalService} from 'projects/split/src/lib/split-panes/split-direction-horizontal.service';

describe('SplitDirectionHorizontalService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SplitDirectionHorizontalService]
    });
  });

  it('should be created', inject([SplitDirectionHorizontalService], (service: SplitDirectionHorizontalService) => {
    expect(service).toBeTruthy();
  }));

  it('should divToSize', inject([SplitDirectionHorizontalService], (service: SplitDirectionHorizontalService) => {
    expect(service.divToSize({clientWidth: 42})).toBe(42);
  }));

  it('should eventToSize', inject([SplitDirectionHorizontalService], (service: SplitDirectionHorizontalService) => {
    expect(service.eventToSize({clientX: 42} as MouseEvent)).toBe(42);
  }));
});
