import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DownloadMenuItemComponent} from './download-menu-item.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('DownloadMenuItemComponent', () => {
  let component: DownloadMenuItemComponent;
  let fixture: ComponentFixture<DownloadMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DownloadMenuItemComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
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
