import {Pipe, PipeTransform} from '@angular/core';
import {StringToolsService} from './string-tools.service';
import * as _ from 'lodash';

@Pipe({
  name: 'prettyString'
})
export class PrettyStringPipe implements PipeTransform {

  constructor(private stringToolsService: StringToolsService) {
  }

  transform(value: string, args?: any): string {
    return _.upperFirst(this.stringToolsService.replaceAll(value.toLowerCase(), '_', ' '));
  }

}
