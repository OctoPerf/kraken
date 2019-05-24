import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImportHarDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/import-har-dialog/import-har-dialog.component';
import {MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';

describe('ImportHarDialogComponent', () => {
  let component: ImportHarDialogComponent;
  let fixture: ComponentFixture<ImportHarDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [ImportHarDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(ImportHarDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportHarDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return simulationPackage', () => {
    expect(component.simulationPackage.value).toBe('');
  });

  it('should return simulationClass', () => {
    expect(component.simulationClass.value).toBe('');
  });
});
