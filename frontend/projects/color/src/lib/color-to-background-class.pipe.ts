import { Pipe, PipeTransform } from '@angular/core';
import {Color} from './color';

@Pipe({
  name: 'colorToBackgroundClass',
  pure: true,
})
export class ColorToBackgroundClassPipe implements PipeTransform {

  transform(value: Color): any {
    return `background-${value}`;
  }

}
