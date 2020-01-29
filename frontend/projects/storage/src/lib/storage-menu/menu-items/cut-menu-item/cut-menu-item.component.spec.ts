import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CutMenuItemComponent} from './cut-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('CutMenuItemComponent', () => {
  let component: CutMenuItemComponent;
  let fixture: ComponentFixture<CutMenuItemComponent>;
  let fileNode: StorageNode;
  let treeControl: SpyObj<StorageTreeControlService>;

  beforeEach(async(() => {
    treeControl = storageTreeControlServiceSpy();
    fileNode = testStorageFileNode();

    TestBed.configureTestingModule({
      declarations: [CutMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: CopyPasteService, useValue: copyPasteServiceSpy()},
        {provide: STORAGE_ID, useValue: 'storage'},
      ]
    })
      .overrideTemplate(CutMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CutMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should handle key', () => {
    const control = component.treeControl as any;
    control.hasSelection = true;
    control.selected = fileNode;
    const spy = jasmine.createSpy();
    component._handleKey(spy);
    expect(spy).toHaveBeenCalledWith(fileNode);
  });

  it('should handle key no _selection', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSelection = false;
    const spy = jasmine.createSpy();
    component._handleKey(spy);
    expect(spy).not.toHaveBeenCalled();
  });

  it('should cut key binding', () => {
    const binding = component.binding;
    expect(binding.keys).toEqual(['ctrl + x']);
    spyOn(component, '_handleKey');
    binding.binding(null);
    expect(component._handleKey).toHaveBeenCalledWith(jasmine.any(Function));
  });

});
