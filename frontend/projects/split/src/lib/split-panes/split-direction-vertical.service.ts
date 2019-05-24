import {Injectable} from '@angular/core';
import {SplitDirectionService} from 'projects/split/src/lib/split-panes/split-direction-service';

@Injectable()
export class SplitDirectionVerticalService implements SplitDirectionService {

  fxLayout = 'column';

  divToSize(nativeElement: any): number {
    return nativeElement.clientHeight;
  }

  eventToSize($event: MouseEvent): number {
    return $event.clientY;
  }
}
