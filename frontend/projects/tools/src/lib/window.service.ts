import * as _ from 'lodash';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WindowService {

  window = window;

  _resizeDebounced = _.debounce(this._resize.bind(this), 50, {'maxWait': 1000});

  resize() {
    this._resizeDebounced();
  }

  resizeNow() {
    setTimeout(this._resize.bind(this));
  }

  _resize() {
    this.window.dispatchEvent(new Event('resize'));
  }

  open(url: Observable<string>) {
    const tab = this.window.open();
    url.subscribe((tabLocation) => tab.location.href = encodeURI(tabLocation), () => tab.close());
  }

  url(): string {
    return `${window.location.protocol}//${window.location.hostname}:${window.location.port}`;
  }

  reload(): void {
    window.location.reload();
  }
}
