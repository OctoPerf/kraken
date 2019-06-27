import {InjectionToken} from '@angular/core';
import {StorageNodeEditor} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {ComponentType} from '@angular/cdk/portal';

export interface EditorMatcher {
  regexp: RegExp | string;
  editor: ComponentType<StorageNodeEditor>;
  helpPageId: HelpPageId;
}

export const STORAGE_EDITORS_MAPPING = new InjectionToken<EditorMatcher[]>('StorageEditorsMapping');
export const STORAGE_DEFAULT_EDITOR = new InjectionToken<ComponentType<StorageNodeEditor>>('StorageDefaultEditor');
