import {TestBed} from '@angular/core/testing';

import {CopyPasteService} from './copy-paste.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageNodes,
  testStorageRootNode
} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';

export const copyPasteServiceSpy = () => jasmine.createSpyObj('CopyPasteService', [
  'cut',
  'copy',
  'paste',
  'isCut',
  'canPaste',
  'clearSelection',
  'ngOnDestroy',
]);

describe('CopyPasteService', () => {

  let service: CopyPasteService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let nodes: StorageNode[];
  let fileNode: StorageNode;
  let directoryNode: StorageNode;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        CopyPasteService,
        StorageNodeToPredicatePipe,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(CopyPasteService);
    httpTestingController = TestBed.get(HttpTestingController);
    nodes = testStorageNodes();
    fileNode = testStorageFileNode();
    directoryNode = testStorageDirectoryNode();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    service.ngOnDestroy();
  });

  it('should cut nodes', () => {
    spyOn(service, 'clearSelection');
    service.cut(nodes);
    expect(service.clearSelection).toHaveBeenCalled();
    expect(service._cutNodes.selected.length).toBe(nodes.length);
  });

  it('should copy nodes', () => {
    spyOn(service, 'clearSelection');
    service.copy(nodes);
    expect(service.clearSelection).toHaveBeenCalled();
    expect(service._copiedNodes.selected.length).toBe(nodes.length);
  });

  it('should return isCut', () => {
    service.cut([fileNode]);
    expect(service.isCut(fileNode)).toBeTruthy();
    expect(service.isCut(directoryNode)).toBeFalsy();
  });

  it('should return canPaste and clear selection', () => {
    expect(service.canPaste()).toBeFalsy();
    service.cut([fileNode]);
    expect(service.canPaste()).toBeTruthy();
    service.copy([fileNode]);
    expect(service.canPaste()).toBeTruthy();
    service.clearSelection();
    expect(service.canPaste()).toBeFalsy();
  });

  it('should remove deleted file (copy)', () => {
    service.copy([fileNode]);
    service._fileDeleted(new NodeDeletedEvent(fileNode));
    expect(service.canPaste()).toBeFalsy();
  });

  it('should remove deleted file (cut)', () => {
    service.cut([fileNode]);
    service._fileDeleted(new NodeDeletedEvent(fileNode));
    expect(service.canPaste()).toBeFalsy();
  });

  it('should copy / paste', () => {
    service.copy([fileNode]);
    service.paste(testStorageRootNode());
    const req = httpTestingController.expectOne(request => request.method === 'POST' && request.url === 'storageApiUrl/copy');
    expect(req.request.params.get('destination')).toEqual('');
    expect(req.request.body).toEqual([fileNode.path]);
    req.flush('');
    httpTestingController.verify();
  });

  it('should cut / paste', () => {
    service.cut([fileNode]);
    service.paste(directoryNode);
    const req = httpTestingController.expectOne(request => request.method === 'POST' && request.url === 'storageApiUrl/move');
    expect(req.request.params.get('destination')).toEqual(directoryNode.path);
    expect(req.request.body).toEqual([fileNode.path]);
    req.flush('');
    httpTestingController.verify();
  });

});
