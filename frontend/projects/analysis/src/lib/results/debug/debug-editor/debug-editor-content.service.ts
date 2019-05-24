import {Injectable} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {HttpClient} from '@angular/common/http';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {of, zip} from 'rxjs';
import {tap} from 'rxjs/operators';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

@Injectable()
export class DebugEditorContentService extends StorageNodeEditorContentService {

  private static readonly HEADER_HEIGHT = '36px';
  private static readonly HEADER_HEIGHT_2X = '72px';

  public _chunk: DebugChunk;

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
              private analysisConfiguration: AnalysisConfigurationService,
              private window: WindowService,
              private toPath: DebugChunkToPathPipe) {
    super(configuration, storage, http, eventBus);
  }

  public load(node: StorageNode) {
    this._node = node;
    this.storage.getJSON(node).subscribe((chunk: DebugChunk) => {
      this._setChunk(chunk);
      const getRequestBody = this.hasRequestBody ? this.storage.getContent({
        path: `${this.toPath.transform(chunk)}/${this._chunk.requestBodyFile}`
      } as StorageNode).pipe(tap(requestBody => this.requestBody = requestBody)) : of('ok');
      const getResponseBody = this.hasResponseBody ? this.storage.getContent({
        path: this._responseBodyPath
      } as StorageNode).pipe(tap(responseBody => this.responseBody = responseBody)) : of('ok');
      zip(getRequestBody, getResponseBody).subscribe(() => this.state = 'loaded', () => this.state = 'error');
    }, () => this.state = 'error');
  }

  private _setChunk(chunk: DebugChunk) {
    this._chunk = chunk;
    this.hasRequestCookies = !!chunk.requestCookies.length;
    this.hasRequestBody = !!chunk.requestBodyFile.length;
    this.hasResponseBody = !!chunk.responseBodyFile.length;

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
    return `${this.toPath.transform(this._chunk)}/${this._chunk.responseBodyFile}`;
  }

  public get chunk(): DebugChunk {
    return this._chunk;
  }

  public openResponseBody() {
    const url = of(this.analysisConfiguration.staticApiUrl('/' + this._responseBodyPath));
    this.window.open(url);
  }

}
