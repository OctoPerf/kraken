import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {DockerComposeEditorComponent} from 'projects/docker/src/lib/docker-compose/docker-compose-editor/docker-compose-editor.component';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import {Command} from 'projects/command/src/lib/entities/command';

describe('DockerComposeEditorComponent', () => {
  let component: DockerComposeEditorComponent;
  let fixture: ComponentFixture<DockerComposeEditorComponent>;
  let node: StorageNode;
  let contentService: StorageNodeEditorContentService;
  let commandService: SpyObj<CommandService>;
  let toParentPath: StorageNodeToParentPathPipe;

  beforeEach(async(() => {
    node = testStorageFileNode();
    contentService = storageNodeEditorContentServiceSpy();
    commandService = commandServiceSpy();
    toParentPath = new StorageNodeToParentPathPipe();

    TestBed.configureTestingModule({
      declarations: [DockerComposeEditorComponent],
      providers: [
        {provide: STORAGE_NODE, useValue: node},
        {provide: CommandService, useValue: commandService},
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

  it('should up', () => {
    commandService.executeCommand.and.returnValue(of('commandId'));
    component.up();
    expect(commandService.executeCommand).toHaveBeenCalledWith(new Command([ 'docker-compose', '--no-ansi', 'up', '-d', '--no-color'], {}, 'spotbugs'));
  });

  it('should down', () => {
    commandService.executeCommand.and.returnValue(of('commandId'));
    component.down();
    expect(commandService.executeCommand).toHaveBeenCalledWith(new Command([ 'docker-compose', '--no-ansi', 'down'], {}, 'spotbugs'));
  });

  it('should ps', () => {
    commandService.executeCommand.and.returnValue(of('commandId'));
    component.ps();
    expect(commandService.executeCommand).toHaveBeenCalledWith(new Command([ 'docker-compose', '--no-ansi', 'ps'], {}, 'spotbugs'));
  });

  it('should logs', () => {
    commandService.executeCommand.and.returnValue(of('commandId'));
    component.logs();
    expect(commandService.executeCommand).toHaveBeenCalledWith(new Command([ 'docker-compose', '--no-ansi', 'logs', '--no-color'], {}, 'spotbugs'));
  });
});
