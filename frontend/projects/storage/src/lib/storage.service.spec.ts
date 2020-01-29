import {TestBed} from '@angular/core/testing';

import {StorageService} from './storage.service';
import {cold} from 'jasmine-marbles';
import {of} from 'rxjs';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import {DeleteFilesDialogComponent} from 'projects/storage/src/lib/storage-dialogs/delete-files-dialog/delete-files-dialog.component';
import {DeleteFilesEvent} from 'projects/storage/src/lib/events/delete-files-event';
import {FileNameDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-name-dialog/file-name-dialog.component';
import {NewFileEvent} from 'projects/storage/src/lib/events/new-file-event';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageNodes,
  testStorageRootNode
} from 'projects/storage/src/lib/entities/storage-node.spec';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import * as _ from 'lodash';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import SpyObj = jasmine.SpyObj;
import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';

export const storageServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageService', [
    'list',
    'listJSON',
    'edit',
    'rename',
    'deleteFiles',
    'deleteFilesWithConfirm',
    'addFile',
    'addDirectory',
    'upload',
    'download',
    'cut',
    'copy',
    'paste',
    'get',
    'getContent',
    'getJSON',
    'deleteFile',
    'find',
    'filterExisting',
  ]);
  spy.list.and.returnValue(cold('---x|', {x: testStorageNodes()}));
  return spy;
};

describe('StorageService', () => {
  let service: StorageService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let dialogs: SpyObj<DialogService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        StorageService,
        StorageNodeToNamePipe,
        NodeEventToNodePipe,
        PathToNamePipe,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    dialogs = TestBed.get(DialogService);
    service = TestBed.get(StorageService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should edit', () => {
    const node = testStorageDirectoryNode();
    service.edit(node);
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenNodeEvent(node));
  });

  it('should rename', () => {
    const fileNode = testStorageFileNode();
    const directoryNode = testStorageFileNode();
    dialogs.open.and.returnValue(of('newName.html'));
    service.rename(fileNode, directoryNode);
    expect(dialogs.open).toHaveBeenCalledWith(FileNameDialogComponent, DialogSize.SIZE_SM, {
      title: 'Rename File',
      name: 'main.html'
    });
    const req = httpTestingController.expectOne(request => request.method === 'POST' && request.url === 'storageApiUrl/files/rename');
    expect(req.request.method).toBe('POST');
    expect(req.request.params.get('directoryPath')).toEqual(directoryNode.path);
    expect(req.request.params.get('oldName')).toEqual('main.html');
    expect(req.request.params.get('newName')).toEqual('newName.html');
    req.flush('');
  });

  it('should deleteFiles', () => {
    const nodes = testStorageNodes();
    service.deleteFiles(nodes);
    const req = httpTestingController.expectOne('storageApiUrl/files/delete');
    expect(req.request.method).toBe('POST');
    req.flush([true]);
    expect(eventBus.publish).toHaveBeenCalledWith(new DeleteFilesEvent([true]));
  });

  it('should deleteFilesWithConfirm', () => {
    const nodes = testStorageNodes();
    dialogs.open.and.returnValue(of('close'));
    service.deleteFilesWithConfirm(nodes);
    expect(dialogs.open).toHaveBeenCalledWith(DeleteFilesDialogComponent, DialogSize.SIZE_LG, {nodes});
    const req = httpTestingController.expectOne('storageApiUrl/files/delete');
    expect(req.request.method).toBe('POST');
    req.flush([true]);
    expect(eventBus.publish).toHaveBeenCalledWith(new DeleteFilesEvent([true]));
  });

  it('should addFile', () => {
    const node = testStorageRootNode();
    dialogs.open.and.returnValue(of('filename'));
    service.addFile(node);
    expect(dialogs.open).toHaveBeenCalledWith(FileNameDialogComponent, DialogSize.SIZE_SM, {
      title: 'New File',
      name: '',
      helpPageId: 'ADMIN_CREATE_FILE'
    });
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/set/content');
    expect(req.request.method).toEqual('POST');
    expect(req.request.params.get('path')).toEqual('filename');
    req.flush(node);
    expect(eventBus.publish).toHaveBeenCalledWith(new NewFileEvent(node));
  });

  it('should addDirectory', () => {
    const node = testStorageDirectoryNode();
    dialogs.open.and.returnValue(of('filename'));
    service.addDirectory(node);
    expect(dialogs.open).toHaveBeenCalledWith(FileNameDialogComponent, DialogSize.SIZE_SM, {
      title: 'New Directory',
      name: '',
      helpPageId: 'ADMIN_CREATE_FILE'
    });
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/set/directory');
    expect(req.request.method).toEqual('POST');
    expect(req.request.params.get('path')).toEqual(node.path + '/filename');
    req.flush(node);
    expect(eventBus.publish).toHaveBeenCalledWith(new NewFileEvent(node));
  });

  it('should upload', () => {
    const node = testStorageDirectoryNode();
    dialogs.open.and.returnValue(of('filename'));
    service.upload(node);
    expect(dialogs.open).toHaveBeenCalledWith(FileUploadDialogComponent,
      DialogSize.SIZE_MD,
      {
        endpoint: 'storageApiUrl/files/set/file?path=spotbugs',
        multiple: true,
        accept: '*',
        title: 'Upload Files',
      });
  });

  it('should download', () => {
    const node = testStorageDirectoryNode();
    expect(service.downloadLink(node)).toBe('storageApiUrl/files/get/file?path=' + node.path);
    expect(service.downloadLink()).toBe('storageApiUrl/files/get/file?path=');
  });

  it('should getContent', () => {
    const node = testStorageFileNode();
    service.getContent(node).subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/get/content');
    expect(req.request.method).toEqual('GET');
    expect(req.request.params.get('path')).toEqual(node.path);
    req.flush('content');
  });

  it('should getJSON', () => {
    const node = testStorageFileNode();
    const response = {key: 'value'};
    service.getJSON(node).subscribe(value => expect(value).toEqual(response));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/get/json');
    expect(req.request.method).toEqual('GET');
    expect(req.request.params.get('path')).toEqual(node.path);
    req.flush(response);
  });

  it('should get', () => {
    const node = testStorageFileNode();
    service.get(node.path).subscribe(value => expect(value).toEqual(node));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/get');
    expect(req.request.method).toEqual('GET');
    expect(req.request.params.get('path')).toEqual(node.path);
    req.flush(node);
  });

  it('should listJSON', () => {
    const nodes = testStorageNodes();
    const response = [{key: 'value'}, {key: 'value'}];
    service.listJSON(nodes).subscribe(value => expect(value).toEqual(response));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/list/json');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(_.map(nodes, 'path'));
    req.flush(response);
  });

  it('should deleteFile', () => {
    const node = testStorageFileNode();
    service.deleteFile(node.path).subscribe();
    const req = httpTestingController.expectOne('storageApiUrl/files/delete');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual([node.path]);
    req.flush([true]);
  });

  it('should find', () => {
    service.find('rootPath', 'matcher', 42).subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/find');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('rootPath')).toEqual('rootPath');
    expect(req.request.params.get('matcher')).toEqual('matcher');
    expect(req.request.params.get('maxDepth')).toEqual('42');
    req.flush([testStorageFileNode()]);
  });

  it('should find defaults', () => {
    service.find('rootPath').subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/find');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('rootPath')).toEqual('rootPath');
    expect(req.request.params.has('matcher')).toBeFalsy();
    expect(req.request.params.has('maxDepth')).toBeFalsy();
    req.flush([testStorageFileNode()]);
  });

  it('should filter existing', () => {
    const nodes = testStorageNodes();
    service.filterExisting(nodes).subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/filter/existing');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nodes);
    req.flush(nodes);
  });

  it('should list', () => {
    service.list().subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/list');
    expect(req.request.method).toEqual('GET');
    req.flush(testStorageNodes());
  });

});

