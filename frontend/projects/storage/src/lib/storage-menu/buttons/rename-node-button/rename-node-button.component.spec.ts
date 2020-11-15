import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {RenameNodeButtonComponent} from './rename-node-button.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';

describe('RenameNodeButtonComponent', () => {
  let component: RenameNodeButtonComponent;
  let fixture: ComponentFixture<RenameNodeButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [RenameNodeButtonComponent],
      providers: [
        {provide: StorageTreeDataSourceService, useValue: storageTreeDataSourceServiceSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
      ]
    })
      .overrideTemplate(RenameNodeButtonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenameNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
