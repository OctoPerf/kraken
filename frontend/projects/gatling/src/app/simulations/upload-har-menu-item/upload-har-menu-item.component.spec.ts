import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {UploadHarMenuItemComponent} from './upload-har-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('UploadHarMenuItemComponent', () => {
  let component: UploadHarMenuItemComponent;
  let fixture: ComponentFixture<UploadHarMenuItemComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [UploadHarMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(UploadHarMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadHarMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
