import {Injectable} from '@angular/core';
import {SplitDirectionService} from 'projects/split/src/lib/split-panes/split-direction-service';

@Injectable()
export class SplitDirectionHorizontalService implements SplitDirectionService {

  fxLayout = 'row';

  divToSize(nativeElement: any): number {
    return nativeElement.clientWidth;
  }

  eventToSize($event: MouseEvent): number {
    return $event.clientX;
  }
}
