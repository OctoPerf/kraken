import {InjectionToken} from '@angular/core';
import {ComponentType} from '@angular/cdk/typings/portal';
import {StorageNodeEditor} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';

export interface EditorMatcher {
  regexp: RegExp | string;
  editor: ComponentType<StorageNodeEditor>;
}

export const STORAGE_EDITORS_MAPPING = new InjectionToken<EditorMatcher[]>('StorageEditorsMapping');
export const STORAGE_DEFAULT_EDITOR = new InjectionToken<ComponentType<StorageNodeEditor>>('StorageDefaultEditor');
