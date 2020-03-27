import { TestBed } from '@angular/core/testing';

import { SecurityGuard } from './security.guard';

describe('SecurityGuard', () => {
  let guard: SecurityGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(SecurityGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
