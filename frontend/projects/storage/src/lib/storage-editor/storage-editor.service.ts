import {Inject, Injectable, Injector, Optional} from '@angular/core';
import {
  EditorMatcher,
  STORAGE_DEFAULT_EDITOR,
  STORAGE_EDITORS_MAPPING,
} from 'projects/storage/src/lib/storage-editors-mapping';
import {ComponentPortal} from '@angular/cdk/portal';
import {
  STORAGE_NODE,
  StorageNodeEditor
} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {MarkdownStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/markdown-storage-node-editor/markdown-storage-node-editor.component';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

@Injectable()
export class StorageEditorService {

  private readonly defaultMatchers: EditorMatcher[] = [
    {regexp: /.*\.markdown/, editor: MarkdownStorageNodeEditorComponent, helpPageId: 'EDITOR_MARKDOWN'},
    {regexp: /.*\.md/, editor: MarkdownStorageNodeEditorComponent, helpPageId: 'EDITOR_MARKDOWN'},
  ];

  editorsMapping: EditorMatcher[];
  defaultEditor: any /*ComponentType<StorageNodeEditor>*/;

  constructor(@Optional() @Inject(STORAGE_EDITORS_MAPPING) _editorsMapping: EditorMatcher[],
              @Optional() @Inject(STORAGE_DEFAULT_EDITOR) _defaultEditor: any /*ComponentType<StorageNodeEditor>*/,
              private injector: Injector) {
    this.defaultEditor = _defaultEditor ? _defaultEditor : DefaultStorageNodeEditorComponent;
    this.editorsMapping = _editorsMapping ? _.concat(_editorsMapping, this.defaultMatchers) : this.defaultMatchers;
    // AOT compiler does not support RegExp. So mwe must convert strings to RegExp at runtime in production
    _.forEach(this.editorsMapping, (matcher: EditorMatcher) => {
      const regexp = matcher.regexp;
      if (_.isString(regexp)) {
        matcher.regexp = new RegExp(regexp);
      }
    });
  }

  getNodeEditor(node: StorageNode): ComponentPortal<StorageNodeEditor> {
    const matcher: EditorMatcher = _.find(this.editorsMapping,
      (current: EditorMatcher) => node.path.match(current.regexp)) as EditorMatcher;
    const editor /*: ComponentType<StorageNodeEditor>*/ = matcher ? matcher.editor : this.defaultEditor;
    return new ComponentPortal(editor, null,
      Injector.create({
        providers: [
          {provide: STORAGE_NODE, useValue: node},
        ],
        parent: this.injector
      }));
  }

  getHelpPageId(node: StorageNode): HelpPageId {
    const matcher: EditorMatcher = _.find(this.editorsMapping,
      (current: EditorMatcher) => node.path.match(current.regexp)) as EditorMatcher;
    return matcher ? matcher.helpPageId : 'ADMIN_FILE_EDITOR';
  }
}
