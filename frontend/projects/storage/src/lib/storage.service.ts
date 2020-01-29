import {Injectable} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {flatMap} from 'rxjs/operators';
import * as _ from 'lodash';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import {DeleteFilesDialogComponent} from 'projects/storage/src/lib/storage-dialogs/delete-files-dialog/delete-files-dialog.component';
import {FileNameDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-name-dialog/file-name-dialog.component';
import {DeleteFilesEvent} from 'projects/storage/src/lib/events/delete-files-event';
import {NewFileEvent} from 'projects/storage/src/lib/events/new-file-event';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

@Injectable()
export class StorageService {

  constructor(private configuration: StorageConfigurationService,
              private eventBus: EventBusService,
              private dialogs: DialogService,
              private http: HttpClient,
              private toNode: NodeEventToNodePipe,
              private toName: StorageNodeToNamePipe) {
  }

  list(): Observable<StorageNode[]> {
    return this.http.get<StorageNode[]>(this.configuration.storageApiUrl('/list'));
  }

  edit(node: StorageNode) {
    this.eventBus.publish(new OpenNodeEvent(node));
  }

  rename(node: StorageNode, parentDirectory: StorageNode) {
    const oldName = this.toName.transform(node);
    const directoryPath = parentDirectory.path;
    this.dialogs.open(FileNameDialogComponent, DialogSize.SIZE_SM, {title: 'Rename File', name: oldName})
      .pipe(flatMap((newName: string) => {
        return this.http.post<StorageNode>(this.configuration.storageApiUrl('/rename'), {}, {
          params: {
            directoryPath,
            oldName,
            newName,
          }
        });
      }))
      .subscribe();
  }

  deleteFiles(nodes: StorageNode[]) {
    return this.http.post<boolean[]>(this.configuration.storageApiUrl('/delete'), _.map(nodes, 'path'))
      .subscribe((results: boolean[]) => {
        this.eventBus.publish(new DeleteFilesEvent(results));
      });
  }

  deleteFilesWithConfirm(nodes: StorageNode[]) {
    this.dialogs.open(DeleteFilesDialogComponent, DialogSize.SIZE_LG, {nodes})
      .subscribe(() => this.deleteFiles(nodes));
  }

  addFile(parent: StorageNode) {
    this._add(parent, 'New File', '/set/content', 'New file');
  }

  addDirectory(parent: StorageNode) {
    this._add(parent, 'New Directory', '/set/directory', null);
  }

  private _add(parent: StorageNode, title: string, path: string, content?: string) {
    this.dialogs.open(FileNameDialogComponent, DialogSize.SIZE_SM, {title, name: '', helpPageId: 'ADMIN_CREATE_FILE'})
      .pipe(flatMap((name: string) => {
        return this.http.post<StorageNode>(this.configuration.storageApiUrl(path), content, {
          params: {
            path: parent.path ? `${parent.path}/${name}` : name
          }
        });
      }))
      .subscribe((node: StorageNode) => {
        this.eventBus.publish(new NewFileEvent(node));
      });
  }

  upload(parent: StorageNode) {
    const path = parent.path;
    const endpoint = this.configuration.storageApiUrl(`/set/file?path=${path}`);
    this.dialogs.open(FileUploadDialogComponent, DialogSize.SIZE_MD, {
      endpoint,
      multiple: true,
      accept: '*',
      title: 'Upload Files',
    }).subscribe();
  }

  downloadLink(node?: StorageNode): string {
    const path = node ? node.path : '';
    return this.configuration.storageApiUrl(`/get/file?path=${path}`);
  }

  get(path: string): Observable<StorageNode> {
    return this.http.get<StorageNode>(this.configuration.storageApiUrl('/get'), {
      params: {
        path
      }
    });
  }

  getContent(node: StorageNode): Observable<string> {
    return this.http.get(this.configuration.storageApiUrl('/get/content'), {
      responseType: 'text',
      params: {
        path: node.path
      }
    });
  }

  getJSON<T>(node: StorageNode): Observable<T> {
    return this.http.get<T>(this.configuration.storageApiUrl('/get/json'), {
      params: {
        path: node.path
      }
    });
  }

  listJSON<T>(nodes: StorageNode[]): Observable<T[]> {
    return this.http.post<T[]>(this.configuration.storageApiUrl('/list/json'), _.map(nodes, 'path'));
  }

  deleteFile(path: string): Observable<boolean[]> {
    return this.http.post<boolean[]>(this.configuration.storageApiUrl('/delete'), [path]);
  }

  find(rootPath: string, matcher: string = null, maxDepth: number = null): Observable<StorageNode[]> {
    const params: any = {rootPath};
    if (matcher) {
      params.matcher = matcher;
    }
    if (maxDepth) {
      params.maxDepth = maxDepth.toString();
    }
    return this.http.get<StorageNode[]>(this.configuration.storageApiUrl('/find'), {params});
  }

  filterExisting(nodes: StorageNode[]): Observable<StorageNode[]> {
    return this.http.post<StorageNode[]>(this.configuration.storageApiUrl('/filter/existing'), nodes);
  }
}
