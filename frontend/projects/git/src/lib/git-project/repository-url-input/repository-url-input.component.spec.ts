import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RepositoryUrlInputComponent} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component';
import {FormControl, FormGroup} from '@angular/forms';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';

export const repositoryUrlInputComponentSpy = () => {
  const spy = jasmine.createSpyObj('RepositoryUrlInputComponent', [
    '',
  ]);
  spy.repositoryUrl = new FormControl('', []);
  return spy;
};

describe('RepositoryUrlInputComponent', () => {
  let component: RepositoryUrlInputComponent;
  let fixture: ComponentFixture<RepositoryUrlInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RepositoryUrlInputComponent],
      imports: [VendorsModule],
      providers: []
    })
      .overrideTemplate(RepositoryUrlInputComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryUrlInputComponent);
    component = fixture.componentInstance;
    component.formGroup = new FormGroup({});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return repository URL', () => {
    expect(component.repositoryUrl.value).toBe('');
  });

});
