import {Injectable} from '@angular/core';
import * as _ from 'lodash';

@Injectable({
  providedIn: 'root'
})
export class StringToolsService {

  private static readonly DOTS = '...';

  private static readonly LETTERS: string[] = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
    's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];

  truncStartString(str: string, length: number): string {
    if (str.length > length + StringToolsService.DOTS.length) {
      return StringToolsService.DOTS + str.slice(str.length - length, str.length);
    }
    return str;
  }

  truncEndString(str: string, length: number): string {
    return _.truncate(str, {
      length
    });
  }

  replaceAll(str: string, search: string | RegExp, replacement: string): string {
    return str.split(search).join(replacement);
  }

  escapeRegExp(str: string): string {
    return _.escapeRegExp(str);
  }

  unescapeRegExp(str: string): string {
    // '^', '$', '\', '.', '*', '+', '?', '(', ')', '[', ']', '{', '}', and '|'
    return str ? str.replace(/\\([\^\$\\\.\*\+\?\(\)\[\]\{\}])/g, '$1') : str;
  }

  numberToAlphabet(num: number): string {
    return StringToolsService.LETTERS[num % StringToolsService.LETTERS.length];
  }
}
