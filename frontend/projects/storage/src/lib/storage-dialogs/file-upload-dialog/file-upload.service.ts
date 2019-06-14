import {Injectable} from '@angular/core';
import {HttpClient, HttpEventType, HttpRequest, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import * as _ from 'lodash';

@Injectable()
export class FileUploadService {

  constructor(private http: HttpClient) {
  }

  public upload(files: File[], endpoint: string): Observable<number>[] {
    return _.map(files, (file: File) => this._uploadFile(file, endpoint));
  }

  _uploadFile(file: File, endpoint: string): Observable<number> {
    const progress = new BehaviorSubject<number>(0);
    const formData = this._newFormData();
    formData.append('file', file, file.name);
    const req = new HttpRequest('POST', endpoint, formData, {reportProgress: true});
    this.http.request(req).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        const percentDone = Math.round((100 * event.loaded) / event.total);
        progress.next(percentDone);
      } else if (event instanceof HttpResponse) {
        progress.complete();
      }
    }, () => progress.complete());
    return progress.asObservable();
  }

  _newFormData(): FormData {
    return new FormData();
  }
}
