import {Pipe, PipeTransform} from '@angular/core';
import * as showdown from 'showdown';
import {DomSanitizer} from '@angular/platform-browser';


@Pipe({
  name: 'markdownToHtml'
})
export class MarkdownToHtmlPipe implements PipeTransform {

  private converter = new showdown.Converter();

  constructor(private sanitizer: DomSanitizer) {
  }

  transform(markdown: any, args?: any): any {
    return this.sanitizer.bypassSecurityTrustHtml(this.converter.makeHtml(markdown));
  }

}
