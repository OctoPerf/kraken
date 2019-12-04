import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DescriptionInputComponent } from './description-input.component';

describe('DescriptionInputComponent', () => {
  let component: DescriptionInputComponent;
  let fixture: ComponentFixture<DescriptionInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DescriptionInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DescriptionInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
