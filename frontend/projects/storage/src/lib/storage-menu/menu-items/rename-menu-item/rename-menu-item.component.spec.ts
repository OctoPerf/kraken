import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {RenameMenuItemComponent} from './rename-menu-item.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {testStorageFileNode, testStorageRootNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;

describe('RenameMenuItemComponent', () => {
  let component: RenameMenuItemComponent;
  let fixture: ComponentFixture<RenameMenuItemComponent>;
  let treeControl: SpyObj<StorageTreeControlService>;
  let datasource: SpyObj<StorageTreeDataSourceService>;
  let storage: SpyObj<StorageService>;

  beforeEach(waitForAsync(() => {
    treeControl = storageTreeControlServiceSpy();
    datasource = storageTreeDataSourceServiceSpy();
    storage = storageServiceSpy();

    TestBed.configureTestingModule({
      declarations: [RenameMenuItemComponent],
      providers: [
        {provide: StorageTreeDataSourceService, useValue: datasource},
        {provide: StorageService, useValue: storage},
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: STORAGE_ID, useValue: 'storage'},
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

  it('should rename', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = true;
    ctrl.first = testStorageFileNode();
    datasource.parentNode.and.returnValue(testStorageRootNode());
    const binding = component.binding;
    expect(binding.keys).toEqual(['F2']);
    expect(binding.binding(null)).toBe(true);
    expect(storage.rename).toHaveBeenCalledWith(testStorageFileNode(), testStorageRootNode());
  });

  it('should not rename', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = false;
    const binding = component.binding;
    expect(binding.binding(null)).toBe(false);
  });
});
