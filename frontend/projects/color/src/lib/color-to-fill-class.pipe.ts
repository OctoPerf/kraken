import {Pipe, PipeTransform} from '@angular/core';
import {Color} from './color';

@Pipe({
  name: 'colorToFillClass',
  pure: true,
})
export class ColorToFillClassPipe implements PipeTransform {

  transform(value: Color): any {
    return `fill-${value}`;
  }

}
