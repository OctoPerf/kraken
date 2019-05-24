import {Inject, InjectionToken, Optional, Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';
import {CodeMode} from 'projects/editor/src/lib/code-editor/code-mode';

export const EDITOR_MODE_MATCHERS = new InjectionToken<ModeMatcher[]>('EditorsModeMatchers');

export interface ModeMatcher {
  regexp: RegExp;
  mode: CodeMode;
}

@Pipe({
  name: 'pathToCodeEditorMode'
})
export class PathToCodeEditorModePipe implements PipeTransform {

  private static readonly MATCHERS: ModeMatcher[] = [
    {regexp: /.*Dockerfile$/, mode: 'dockerfile'},
    {regexp: /.*\.css$/, mode: 'css'},
    {regexp: /.*\.html$/, mode: 'html'},
    {regexp: /.*\.ini$/, mode: 'ini'},
    {regexp: /.*\.java$/, mode: 'java'},
    {regexp: /.*\.js$/, mode: 'javascript'},
    {regexp: /.*\.json$/, mode: 'json'},
    {regexp: /.*\.less$/, mode: 'less'},
    {regexp: /.*Makefile$/, mode: 'makefile'},
    {regexp: /.*\.markdown$/, mode: 'markdown'},
    {regexp: /.*\.md$/, mode: 'markdown'},
    {regexp: /.*\.sass$/, mode: 'sass'},
    {regexp: /.*\.scala$/, mode: 'scala'},
    {regexp: /.*\.scss$/, mode: 'scss'},
    {regexp: /.*\.sh$/, mode: 'sh'},
    {regexp: /.*\.sql$/, mode: 'sql'},
    {regexp: /.*\.svg$/, mode: 'svg'},
    {regexp: /.*\.ts$/, mode: 'typescript'},
    {regexp: /.*\.xml$/, mode: 'xml'},
    {regexp: /.*\.yaml$/, mode: 'yaml'},
    {regexp: /.*\.yml$/, mode: 'yaml'},
    {regexp: /.*/, mode: 'text'}
  ];

  private readonly matchers: ModeMatcher[];

  constructor(@Optional() @Inject(EDITOR_MODE_MATCHERS) _matchers: ModeMatcher[]) {
    _matchers = _matchers ? _matchers : [];
    this.matchers = _.concat(_matchers, PathToCodeEditorModePipe.MATCHERS);
  }

  transform(path: string, args?: any): CodeMode {
    const matcher: ModeMatcher = _.find(this.matchers, (current: ModeMatcher) => path.match(current.regexp)) as ModeMatcher;
    return matcher.mode;
  }

}
