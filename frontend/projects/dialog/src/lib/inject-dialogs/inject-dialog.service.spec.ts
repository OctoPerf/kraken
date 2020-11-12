import {TestBed} from '@angular/core/testing';

import {InjectDialogService} from './inject-dialog.service';
import {MatDialog} from '@angular/material/dialog';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {Component} from '@angular/core';

export const injectDialogServiceSpy = () => {
  const spy = jasmine.createSpyObj('InjectDialogService', [
    'open',
  ]);
  return spy;
};

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('InjectDialogService', () => {
  let service: InjectDialogService;
  let dialog: SpyObj<MatDialog>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: MatDialog, useValue: jasmine.createSpyObj('MatDialog', ['open'])},
        InjectDialogService,
      ]
    });
    service = TestBed.inject(InjectDialogService);
    dialog = TestBed.inject(MatDialog) as SpyObj<MatDialog>;
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
});
