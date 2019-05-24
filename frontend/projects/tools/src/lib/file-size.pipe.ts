import { Pipe, PipeTransform } from '@angular/core';

import * as filesize from 'filesize';

@Pipe({
  name: 'fileSize'
})
export class FileSizePipe implements PipeTransform {

  transform(value: any, options?: any): any {
    return filesize(value, options);
  }

}
