import {TestBed} from '@angular/core/testing';

import {DialogService} from './dialog.service';
import { MatDialog } from '@angular/material/dialog';
import {Component} from '@angular/core';
import SpyObj = jasmine.SpyObj;
import {InspectDialogComponent} from 'projects/dialog/src/lib/inspect-dialog/inspect-dialog.component';
import {DeleteDialogComponent} from 'projects/dialog/src/lib/delete-dialog/delete-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/logs-dialog/logs-dialog.component';
import {WaitDialogComponent} from 'projects/dialog/src/lib/wait-dialog/wait-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {of} from 'rxjs';
import {ConfirmDialogComponent} from 'projects/dialog/src/lib/confirm-dialog/confirm-dialog.component';

export const dialogsServiceSpy = () => {
  const spy = jasmine.createSpyObj('DialogService', [
    'open',
    'inspect',
    'delete',
    'confirm',
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

describe('DialogService', () => {

  let service: DialogService;
  let dialog: SpyObj<MatDialog>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MatDialog, useValue: jasmine.createSpyObj('MatDialog', ['open'])},
      ]
    });
    service = TestBed.get(DialogService);
    dialog = TestBed.get(MatDialog);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should open/close dialog', () => {
    dialog.open.and.returnValue({
      afterClosed: () => of('result')
    } as any);
    service.open(TestComponent, DialogSize.SIZE_MD, {key: 'value'}).subscribe((result) => expect(result).toBe('result'));
    expect(dialog.open).toHaveBeenCalledWith(TestComponent, {
      panelClass: DialogSize.SIZE_MD,
      data: {key: 'value'}
    });
  });

  it('should open/dismiss dialog', () => {
    dialog.open.and.returnValue({
      afterClosed: () => of(undefined)
    } as any);
    service.open(TestComponent).subscribe(() => fail('should not call callback on dismiss'));
    expect(dialog.open).toHaveBeenCalledWith(TestComponent, {
      panelClass: DialogSize.SIZE_SM,
      data: undefined,
    });
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

  it('should delete', () => {
    const spy = spyOn(service, 'open');
    service.delete('name', ['item'], 'TEST');
    expect(spy).toHaveBeenCalledWith(
      DeleteDialogComponent, DialogSize.SIZE_MD, {
        name: 'name',
        items: ['item'],
        helpPageId: 'TEST'
      });
  });

  it('should confirm', () => {
    const spy = spyOn(service, 'open');
    service.confirm('title', 'message');
    expect(spy).toHaveBeenCalledWith(
      ConfirmDialogComponent, DialogSize.SIZE_MD, {
        title: 'title',
        message: 'message'
      });
  });

  it('should wait', () => {
    service.wait({title: 'title', progress: 50});
    expect(dialog.open).toHaveBeenCalledWith(WaitDialogComponent, {
      panelClass: DialogSize.SIZE_MD,
      data: {title: 'title', progress: 50},
      disableClose: true,
    });
  });
});
