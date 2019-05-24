import {TestBed} from '@angular/core/testing';

import {StorageEditorService} from './storage-editor.service';
import {StorageNodeToExtPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-ext.pipe';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {MarkdownStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/markdown-storage-node-editor/markdown-storage-node-editor.component';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

export const storageEditorServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageEditorService', [
    'getNodeEditor',
  ]);
  return spy;
};

describe('StorageEditorService', () => {
  let service: StorageEditorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        CoreTestModule,
      ],
      providers: [
        StorageEditorService,
        StorageNodeToExtPipe,
      ]
    });
    service = TestBed.get(StorageEditorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be created with custom mapping', () => {
    const _service = new StorageEditorService([
        {regexp: 'txt', editor: MarkdownStorageNodeEditorComponent},
        {regexp: /txt/, editor: MarkdownStorageNodeEditorComponent}
      ],
      MarkdownStorageNodeEditorComponent,
      null);
    expect(_service.defaultEditor).toBe(MarkdownStorageNodeEditorComponent);
    expect(_service.editorsMapping.length).toBe(4);
    expect(_service.editorsMapping[0].regexp).toEqual(new RegExp('txt'));
  });

  it('should return default editor', () => {
    const portal = service.getNodeEditor(testStorageFileNode());
    expect(portal).toBeDefined();
    expect(portal.component).toBe(DefaultStorageNodeEditorComponent);
  });

  it('should return markdown editor', () => {
    const portal = service.getNodeEditor({
      path: 'spotbugs/main.md',
      type: 'FILE',
      depth: 1,
      length: 42,
      lastModified: 1337
    });
    expect(portal).toBeDefined();
    expect(portal.component).toBe(MarkdownStorageNodeEditorComponent);
  });

});
