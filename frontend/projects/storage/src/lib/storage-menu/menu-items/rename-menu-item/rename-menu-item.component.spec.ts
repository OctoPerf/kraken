import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameMenuItemComponent } from './rename-menu-item.component';
import {NewFileMenuItemComponent} from 'projects/storage/src/lib/storage-menu/menu-items/new-file-menu-item/new-file-menu-item.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';

describe('RenameMenuItemComponent', () => {
  let component: RenameMenuItemComponent;
  let fixture: ComponentFixture<RenameMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RenameMenuItemComponent],
      providers: [
        {provide: StorageTreeDataSourceService, useValue: storageTreeDataSourceServiceSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(RenameMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenameMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
