import {Injectable} from '@angular/core';
import {Retry} from 'projects/tools/src/lib/retry';

@Injectable({
  providedIn: 'root'
})
export class RetriesService {

  get() {
    return new Retry();
  }
}
