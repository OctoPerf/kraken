import { TestBed } from '@angular/core/testing';

import { RuntimeService } from './runtime.service';

describe('RuntimeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RuntimeService = TestBed.get(RuntimeService);
    expect(service).toBeTruthy();
  });
});
