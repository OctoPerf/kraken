import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PasteMenuItemComponent} from './paste-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {testStorageDirectoryNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('PasteMenuItemComponent', () => {
  let component: PasteMenuItemComponent;
  let fixture: ComponentFixture<PasteMenuItemComponent>;
  let directoryNode: StorageNode;
  let treeControl: SpyObj<StorageTreeControlService>;
  let copyPaste: SpyObj<CopyPasteService>;

  beforeEach(async(() => {
    treeControl = storageTreeControlServiceSpy();
    copyPaste = copyPasteServiceSpy();
    directoryNode = testStorageDirectoryNode();
    TestBed.configureTestingModule({
      declarations: [PasteMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: CopyPasteService, useValue: copyPaste},
        {provide: STORAGE_ID, useValue: 'storage'},
      ]
    })
      .overrideTemplate(PasteMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasteMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should paste key binding multi file _selection', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleOrNoSelection = true;
    ctrl.selectedDirectory = directoryNode;
    const binding = component.binding;
    expect(binding.keys).toEqual(['ctrl + v']);
    expect(binding.binding(null)).toBe(true);
    expect(copyPaste.paste).toHaveBeenCalledWith(directoryNode);
  });

  it('should paste key no selection', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleOrNoSelection = false;
    const binding = component.binding;
    expect(binding.keys).toEqual(['ctrl + v']);
    expect(binding.binding(null)).toBe(false);
    expect(copyPaste.paste).not.toHaveBeenCalled();
  });
});
