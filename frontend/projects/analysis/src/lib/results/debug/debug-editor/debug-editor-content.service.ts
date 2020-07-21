import {Injectable} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {HttpClient} from '@angular/common/http';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {of, zip} from 'rxjs';
import {tap} from 'rxjs/operators';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';
import {StorageStaticService} from 'projects/storage/src/lib/storage-static.service';

@Injectable()
export class DebugEditorContentService extends StorageNodeEditorContentService {

  private static readonly HEADER_HEIGHT = '36px';
  private static readonly HEADER_HEIGHT_2X = '72px';

  public _entry: DebugEntry;

  public requestHeadersFlex: string;
  public requestCookiesFlex: string;
  public requestBodyFlex: string;
  public responseHeadersFlex: string;
  public responseBodyFlex: string;

  public hasRequestCookies: boolean;
  public hasRequestBody: boolean;
  public hasResponseBody: boolean;

  public requestBody: string;
  public responseBody: string;

  constructor(configuration: StorageConfigurationService,
              storage: StorageService,
              http: HttpClient,
              eventBus: EventBusService,
              private storageStaticService: StorageStaticService,
              private toPath: DebugEntryToPathPipe) {
    super(configuration, storage, http, eventBus);
  }

  public load(node: StorageNode) {
    this._node = node;
    this.storage.getJSON(node).subscribe((entry: DebugEntry) => {
      this._setEntry(entry);
      const getRequestBody = this.hasRequestBody ? this.storage.getContent({
        path: `${this.toPath.transform(entry)}/${this._entry.requestBodyFile}`
      } as StorageNode).pipe(tap(requestBody => this.requestBody = requestBody)) : of('ok');
      const getResponseBody = this.hasResponseBody ? this.storage.getContent({
        path: this._responseBodyPath
      } as StorageNode).pipe(tap(responseBody => this.responseBody = responseBody)) : of('ok');
      zip(getRequestBody, getResponseBody).subscribe(() => this.state = 'loaded', () => this.state = 'error');
    }, () => this.state = 'error');
  }

  private _setEntry(entry: DebugEntry) {
    this._entry = entry;
    this.hasRequestCookies = !!entry.requestCookies.length;
    this.hasRequestBody = !!entry.requestBodyFile.length;
    this.hasResponseBody = !!entry.responseBodyFile.length;

    if (this.hasRequestBody) {
      if (this.hasRequestCookies) {
        this.requestBodyFlex = `calc(40% - ${DebugEditorContentService.HEADER_HEIGHT})`;
        this.requestCookiesFlex = '25%';
        this.requestHeadersFlex = `calc(35% - ${DebugEditorContentService.HEADER_HEIGHT_2X})`;
      } else {
        this.requestBodyFlex = `calc(45% - ${DebugEditorContentService.HEADER_HEIGHT})`;
        this.requestHeadersFlex = `calc(55% - ${DebugEditorContentService.HEADER_HEIGHT_2X})`;
      }
    } else {
      if (this.hasRequestCookies) {
        this.requestCookiesFlex = '35%';
        this.requestHeadersFlex = `calc(65% - ${DebugEditorContentService.HEADER_HEIGHT_2X})`;
      } else {
        this.requestHeadersFlex = `calc(100% - ${DebugEditorContentService.HEADER_HEIGHT_2X})`;
      }
    }
    if (this.hasResponseBody) {
      this.responseBodyFlex = `calc(60% - ${DebugEditorContentService.HEADER_HEIGHT})`;
      this.responseHeadersFlex = `calc(40% - ${DebugEditorContentService.HEADER_HEIGHT})`;
    } else {
      this.responseHeadersFlex = `calc(100% - ${DebugEditorContentService.HEADER_HEIGHT})`;
    }
  }

  private get _responseBodyPath(): string {
    return `${this.toPath.transform(this._entry)}/${this._entry.responseBodyFile}`;
  }

  public get entry(): DebugEntry {
    return this._entry;
  }

  public openResponseBody() {
    this.storageStaticService.openStaticPage(this._responseBodyPath);
  }

}
