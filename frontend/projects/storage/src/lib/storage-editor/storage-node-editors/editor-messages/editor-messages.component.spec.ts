import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorMessagesComponent } from './editor-messages.component';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';

describe('EditorMessagesComponent', () => {
  let component: EditorMessagesComponent;
  let fixture: ComponentFixture<EditorMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorMessagesComponent ],
      providers: [
        {provide: StorageNodeEditorContentService, useValue: storageNodeEditorContentServiceSpy()}
      ]
    })
      .overrideTemplate(EditorMessagesComponent, '')
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
