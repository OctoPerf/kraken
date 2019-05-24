import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';
import {dockerComposeServiceSpy} from 'projects/docker/src/lib/docker-compose/docker-compose.service.spec';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {DockerComposeEditorComponent} from 'projects/docker/src/lib/docker-compose/docker-compose-editor/docker-compose-editor.component';
import {DockerComposeService} from 'projects/docker/src/lib/docker-compose/docker-compose.service';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';

describe('DockerComposeEditorComponent', () => {
  let component: DockerComposeEditorComponent;
  let fixture: ComponentFixture<DockerComposeEditorComponent>;
  let node: StorageNode;
  let contentService: StorageNodeEditorContentService;
  let dockerComposeService: SpyObj<DockerComposeService>;
  let toParentPath: StorageNodeToParentPathPipe;

  beforeEach(async(() => {
    node = testStorageFileNode();
    contentService = storageNodeEditorContentServiceSpy();
    dockerComposeService = dockerComposeServiceSpy();
    toParentPath = new StorageNodeToParentPathPipe();

    TestBed.configureTestingModule({
      declarations: [DockerComposeEditorComponent],
      providers: [
        {provide: STORAGE_NODE, useValue: node},
        {provide: DockerComposeService, useValue: dockerComposeService},
        {provide: StorageNodeToParentPathPipe, useValue: toParentPath},
      ]
    })
      .overrideProvider(StorageNodeEditorContentService, {useValue: contentService})
      .overrideTemplate(DockerComposeEditorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DockerComposeEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should command', () => {
    dockerComposeService.command.and.returnValue(of('commandId'));
    component.command('up');
    expect(dockerComposeService.command).toHaveBeenCalledWith('up', 'spotbugs');
  });
});
