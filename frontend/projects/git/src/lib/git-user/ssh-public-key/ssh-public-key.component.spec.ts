import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {SshPublicKeyComponent} from './ssh-public-key.component';
import {GitUserService} from 'projects/git/src/lib/git-user/git-user.service';
import {gitUserServiceSpy} from 'projects/git/src/lib/git-user/git-user.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;

describe('SshPublicKeyComponent', () => {
  let component: SshPublicKeyComponent;
  let fixture: ComponentFixture<SshPublicKeyComponent>;
  let gitUserService: SpyObj<GitUserService>;

  beforeEach(waitForAsync(() => {
    gitUserService = gitUserServiceSpy();
    gitUserService.publicKey.and.returnValue(of('publicKey'));
    TestBed.configureTestingModule({
      declarations: [SshPublicKeyComponent],
      providers: [
        {provide: GitUserService, useValue: gitUserService},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SshPublicKeyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.publicKey).toBeDefined();
  });
});
