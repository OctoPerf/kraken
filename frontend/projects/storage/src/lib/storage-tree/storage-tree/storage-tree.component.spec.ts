import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {STORAGE_CONTEXTUAL_MENU, StorageTreeComponent} from './storage-tree.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageNodes
} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageContextualMenuComponent} from 'projects/storage/src/lib/storage-menu/storage-contextual-menu/storage-contextual-menu.component';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import SpyObj = jasmine.SpyObj;
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';
import {StorageKeyBindingService} from 'projects/storage/src/lib/storage-tree/storage-key-binding.service';
import {storageKeyBindingServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-key-binding.service.spec';

describe('StorageTreeComponent', () => {
  let component: StorageTreeComponent;
  let fixture: ComponentFixture<StorageTreeComponent>;
  let treeControl: StorageTreeControlService;
  let dataSource: StorageTreeDataSourceService;
  let eventBus: SpyObj<EventBusService>;
  let fileNode: StorageNode;
  let directoryNode: StorageNode;

  beforeEach(async(() => {
    treeControl = storageTreeControlServiceSpy();
    dataSource = storageTreeDataSourceServiceSpy();
    eventBus = eventBusSpy();

    TestBed.configureTestingModule({
      declarations: [StorageTreeComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: STORAGE_CONTEXTUAL_MENU, useValue: StorageContextualMenuComponent},
        {provide: STORAGE_ID, useValue: 'storage'},
        {provide: EventBusService, useValue: eventBus},
        {provide: StorageKeyBindingService, useValue: storageKeyBindingServiceSpy}
      ]
    })
      .overrideProvider(StorageListService, {useValue: storageListServiceSpy()})
      .overrideProvider(StorageTreeControlService, {useValue: treeControl})
      .overrideProvider(StorageTreeDataSourceService, {useValue: dataSource})
      .overrideProvider(CopyPasteService, {useValue: copyPasteServiceSpy()})
      .overrideProvider(StorageKeyBindingService, {useValue: storageKeyBindingServiceSpy()})
      .overrideTemplate(StorageTreeComponent, '')
      .compileComponents();

    fileNode = testStorageFileNode();
    directoryNode = testStorageDirectoryNode();
  }));

  beforeEach(() => {
    dataSource.data = testStorageNodes();
    fixture = TestBed.createComponent(StorageTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return hasChild', () => {
    expect(component.hasChild(null, fileNode)).toBeFalsy();
    expect(component.hasChild(null, directoryNode)).toBeTruthy();
  });

  it('should selectHelpPage', () => {
    component.selectHelpPage();
    expect(eventBus.publish).toHaveBeenCalledWith(new SelectHelpEvent('storage' as any));
  });
});
