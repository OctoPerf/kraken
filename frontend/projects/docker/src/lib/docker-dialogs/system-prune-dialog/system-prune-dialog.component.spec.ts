import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {SystemPruneDialogComponent} from 'projects/docker/src/lib/docker-dialogs/system-prune-dialog/system-prune-dialog.component';

describe('SystemPruneDialogComponent', () => {
  let component: SystemPruneDialogComponent;
  let fixture: ComponentFixture<SystemPruneDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [SystemPruneDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(SystemPruneDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemPruneDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.all).toBeTruthy();
    expect(component.volumes).toBeTruthy();
  });

  it('should return all', () => {
    expect(component.all.value).toBe(false);
  });

  it('should return volumes', () => {
    expect(component.volumes.value).toBe(false);
  });

});
