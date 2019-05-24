import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImportHarMenuItemComponent} from './import-har-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('ImportHarMenuItemComponent', () => {
  let component: ImportHarMenuItemComponent;
  let fixture: ComponentFixture<ImportHarMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ImportHarMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(ImportHarMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportHarMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
