import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LinkSelectionButtonComponent} from './link-selection-button.component';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageRootNode
} from 'projects/storage/src/lib/entities/storage-node.spec';
import {STORAGE_ROOT_NODE} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';

describe('LinkSelectionButtonComponent', () => {
  let component: LinkSelectionButtonComponent;
  let fixture: ComponentFixture<LinkSelectionButtonComponent>;
  let eventBus: EventBusService;
  let treeControl: StorageTreeControlService;
  let fileNode: StorageNode;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: STORAGE_ID, useValue: 'test'},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: EventBusService, useValue: new EventBusService()},
        {provide: STORAGE_ID, useValue: 'storage'},
        {provide: STORAGE_ROOT_NODE, useValue: testStorageDirectoryNode()},
      ],
      declarations: [ LinkSelectionButtonComponent ]
    }).overrideTemplate(LinkSelectionButtonComponent, '')
    .compileComponents();
    eventBus = TestBed.get(EventBusService);
    treeControl = TestBed.get(StorageTreeControlService);
    fileNode = testStorageFileNode();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkSelectionButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should select node', () => {
    eventBus.publish(new SelectNodeEvent(fileNode));
    expect(treeControl.selectOne).toHaveBeenCalledWith(fileNode);
  });

  it('should not select node', () => {
    component.switchLink();
    eventBus.publish(new SelectNodeEvent(fileNode));
    expect(treeControl.selectOne).not.toHaveBeenCalledWith(fileNode);
  });

  it('should not select node (other tree)', () => {
    eventBus.publish(new SelectNodeEvent({path: 'other/main.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337}));
    expect(treeControl.selectOne).not.toHaveBeenCalledWith(fileNode);
  });

});
