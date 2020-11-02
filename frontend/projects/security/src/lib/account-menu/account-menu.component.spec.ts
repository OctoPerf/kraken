import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {AccountMenuComponent} from './account-menu.component';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {securityServiceSpy} from 'projects/security/src/lib/security.service.spec';

describe('AccountMenuComponent', () => {
  let component: AccountMenuComponent;
  let fixture: ComponentFixture<AccountMenuComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AccountMenuComponent],
      providers: [
        {provide: SecurityService, useValue: securityServiceSpy()},
      ]
    })
      .overrideTemplate(AccountMenuComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
