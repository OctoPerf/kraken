import { Injectable } from '@angular/core';

import * as jsBeautify from 'js-beautify';
const htmlBeautify = jsBeautify.html;
const cssBeautify = jsBeautify.css;

@Injectable({
  providedIn: 'root'
})
export class FormatterService {

  private static readonly SPACES = 2;

  formatJSON(json: string): string {
    return JSON.stringify(JSON.parse(json), null, FormatterService.SPACES);
  }

  formatXML(xml: string): string {
    return this.formatHTML(xml);
  }

  formatHTML(html: string): string {
    return htmlBeautify(html, {
      preserve_newlines: false,
      indent_size: FormatterService.SPACES,
    });
  }

  formatJS(js: string): string {
    return jsBeautify(js, {
      preserve_newlines: false,
      indent_size: FormatterService.SPACES,
    });
  }

  formatCSS(css: string): string {
    return cssBeautify(css, {
      preserve_newlines: false,
      indent_size: FormatterService.SPACES,
    });
  }
}
