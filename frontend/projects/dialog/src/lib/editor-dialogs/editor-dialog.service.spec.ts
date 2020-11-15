import {TestBed} from '@angular/core/testing';

import {EditorDialogService} from 'projects/dialog/src/lib/editor-dialogs/editor-dialog.service';
import {MatDialog} from '@angular/material/dialog';
import {Component} from '@angular/core';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {InspectDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/inspect-dialog/inspect-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/logs-dialog/logs-dialog.component';
import SpyObj = jasmine.SpyObj;

export const editorDialogServiceSpy = () => {
  const spy = jasmine.createSpyObj('EditorDialogService', [
    'open',
    'inspect',
    'logs',
  ]);
  return spy;
};

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('EditorDialogService', () => {

  let service: EditorDialogService;
  let dialog: SpyObj<MatDialog>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MatDialog, useValue: jasmine.createSpyObj('MatDialog', ['open'])},
        EditorDialogService
      ]
    });
    service = TestBed.inject(EditorDialogService);
    dialog = TestBed.inject(MatDialog) as SpyObj<MatDialog>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should inspect', () => {
    service.inspect('name', '{}', 'TEST');
    expect(dialog.open).toHaveBeenCalledWith(InspectDialogComponent, {
      panelClass: DialogSize.SIZE_LG,
      data: {
        object: '{}',
        name: 'name',
        helpPageId: 'TEST'
      }
    });
  });

  it('should logsComponents', () => {
    service.logs('title', 'logs');
    expect(dialog.open).toHaveBeenCalledWith(LogsDialogComponent, {
      panelClass: DialogSize.SIZE_FULL,
      data: {
        title: 'title',
        logs: 'logs',
      }
    });
  });
});
