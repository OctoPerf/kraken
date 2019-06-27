import {ModuleWithProviders, NgModule} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageWatcherService} from 'projects/storage/src/lib/storage-watcher.service';
import {StorageTreeModule} from 'projects/storage/src/lib/storage-tree/storage-tree.module';
import {StorageEditorModule} from 'projects/storage/src/lib/storage-editor/storage-editor.module';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {
  EditorMatcher,
  STORAGE_DEFAULT_EDITOR,
  STORAGE_EDITORS_MAPPING
} from 'projects/storage/src/lib/storage-editors-mapping';
import {StorageNodeEditor} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageDialogsModule} from 'projects/storage/src/lib/storage-dialogs/storage-dialogs.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {ComponentType} from '@angular/cdk/portal';

@NgModule({
  imports: [
    StorageTreeModule,
    StorageEditorModule,
    StorageDialogsModule,
    DateModule,
  ],
  exports: [
    StorageTreeModule,
    StorageEditorModule,
    StorageDialogsModule,
  ],
  providers: [
    StorageService,
    StorageWatcherService,
  ],
})
export class StorageModule {
  public static forRoot(id: string,
                        editors?: EditorMatcher[],
                        defaultEditor?: ComponentType<StorageNodeEditor>): ModuleWithProviders {
    return {
      ngModule: StorageModule,
      providers: [
        {
          provide: STORAGE_ID,
          useValue: id
        },
        {
          provide: STORAGE_EDITORS_MAPPING,
          useValue: editors
        },
        {
          provide: STORAGE_DEFAULT_EDITOR,
          useValue: defaultEditor
        },
      ]
    };
  }
}
