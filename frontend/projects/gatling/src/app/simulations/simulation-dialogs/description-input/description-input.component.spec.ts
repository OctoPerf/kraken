import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DescriptionInputComponent} from './description-input.component';
import {FormGroup} from '@angular/forms';

describe('DescriptionInputComponent', () => {
  let component: DescriptionInputComponent;
  let fixture: ComponentFixture<DescriptionInputComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [DescriptionInputComponent]
    })
      .overrideTemplate(DescriptionInputComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DescriptionInputComponent);
    component = fixture.componentInstance;
    component.formGroup = new FormGroup({});
    component.value = 'description';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return description', () => {
    expect(component.description.value).toBe('description');
  });
});
