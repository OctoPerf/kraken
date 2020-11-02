import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DownloadMenuItemComponent} from './download-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageStaticService} from 'projects/storage/src/lib/storage-static.service';
import {storageStaticServiceSpy} from 'projects/storage/src/lib/storage-static.service.spec';

describe('DownloadMenuItemComponent', () => {
  let component: DownloadMenuItemComponent;
  let fixture: ComponentFixture<DownloadMenuItemComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [DownloadMenuItemComponent],
      providers: [
        {provide: StorageStaticService, useValue: storageStaticServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(DownloadMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DownloadMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
