import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DurationInputComponent} from './duration-input.component';
import {FormControl, FormGroup} from '@angular/forms';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';

export const durationInputComponentSpy = () => {
  const spy = jasmine.createSpyObj('DurationInputComponent', [
    '',
  ]);
  spy.duration = new FormControl('', []);
  spy.unit = new FormControl('', []);
  spy.asString = 'PT5M';
  return spy;
};

describe('DurationInputComponent', () => {
  let component: DurationInputComponent;
  let fixture: ComponentFixture<DurationInputComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [DurationInputComponent],
      providers: [
        {provide: LocalStorageService, useValue: localStorageServiceSpy()}
      ]
    })
      .overrideTemplate(DurationInputComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DurationInputComponent);
    component = fixture.componentInstance;
    component.formGroup = new FormGroup({});
    component.id = 'id';
    component.storageId = 'storageId';
    component.label = 'label';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return duration', () => {
    expect(component.duration.value).toBe(5);
  });

  it('should return unit', () => {
    expect(component.unit.value).toBe('M');
  });

  it('should asString', () => {
    expect(component.asString).toBe('PT5M');
  });
});
