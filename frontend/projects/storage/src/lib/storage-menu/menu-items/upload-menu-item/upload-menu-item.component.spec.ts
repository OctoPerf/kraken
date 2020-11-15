import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {UploadMenuItemComponent} from './upload-menu-item.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('UploadMenuItemComponent', () => {
  let component: UploadMenuItemComponent;
  let fixture: ComponentFixture<UploadMenuItemComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [UploadMenuItemComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(UploadMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
