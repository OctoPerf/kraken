import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DefaultStorageNodeEditorComponent} from './default-storage-node-editor.component';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;

describe('DefaultStorageNodeEditorComponent', () => {
  let component: DefaultStorageNodeEditorComponent;
  let fixture: ComponentFixture<DefaultStorageNodeEditorComponent>;
  let contentService: SpyObj<StorageNodeEditorContentService>;

  beforeEach(waitForAsync(() => {
    contentService = storageNodeEditorContentServiceSpy();
    TestBed.configureTestingModule({
      declarations: [DefaultStorageNodeEditorComponent],
      providers: [
        {provide: StorageNodeEditorContentService, useValue: contentService},
        {provide: STORAGE_NODE, useValue: testStorageFileNode()},
      ]
    })
      .overrideProvider(StorageNodeEditorContentService, {useValue: contentService})
      .overrideTemplate(DefaultStorageNodeEditorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DefaultStorageNodeEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
