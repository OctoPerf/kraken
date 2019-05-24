import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {ExecuteScriptMenuItemComponent} from 'projects/administration/src/app/contextual-menu/execute-script-menu-item/execute-script-menu-item.component';
import {testStorageDirectoryNode, testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;

describe('ExecuteScriptMenuItemComponent', () => {
  let component: ExecuteScriptMenuItemComponent;
  let fixture: ComponentFixture<ExecuteScriptMenuItemComponent>;
  let commands: SpyObj<CommandService>;
  let treeControl: any;

  beforeEach(async(() => {
    commands = commandServiceSpy();
    treeControl = {
      selectedDirectory: testStorageDirectoryNode(),
      first: testStorageFileNode(),
    };

    TestBed.configureTestingModule({
      declarations: [ExecuteScriptMenuItemComponent],
      providers: [
        {provide: CommandService, useValue: commands},
        {provide: StorageTreeControlService, useValue: treeControl},
        StorageNodeToNamePipe,
      ]
    })
      .overrideTemplate(ExecuteScriptMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteScriptMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should execute', () => {
    commands.executeScript.and.returnValue(of('commandId'));
    component.execute();
    expect(commands.executeScript).toHaveBeenCalledWith(testStorageDirectoryNode().path, 'main.html');
  });
});
