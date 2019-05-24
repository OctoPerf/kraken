import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImageNameDialogComponent} from './image-name-dialog.component';
import {MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';

describe('ImageNameDialogComponent', () => {
  let component: ImageNameDialogComponent;
  let fixture: ComponentFixture<ImageNameDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [ImageNameDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()}
      ]
    })
      .overrideTemplate(ImageNameDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageNameDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.image).toBeTruthy();
  });
});
