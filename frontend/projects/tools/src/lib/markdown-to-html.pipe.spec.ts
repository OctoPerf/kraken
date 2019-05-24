import {MarkdownToHtmlPipe} from './markdown-to-html.pipe';

describe('MarkdownToHtmlPipe', () => {
  it('create an instance', () => {
    const pipe = new MarkdownToHtmlPipe({bypassSecurityTrustHtml: (str) => str} as any);
    expect(pipe).toBeTruthy();
    expect(pipe.transform('##TEST')).toBeTruthy();
  });
});
