import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Observable, of} from 'rxjs';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {ConfirmDialogComponent} from 'projects/dialog/src/lib/default-dialogs/confirm-dialog/confirm-dialog.component';
import {DeleteDialogComponent} from 'projects/dialog/src/lib/default-dialogs/delete-dialog/delete-dialog.component';
import {WaitDialogComponent} from 'projects/dialog/src/lib/default-dialogs/wait-dialog/wait-dialog.component';
import {WaitDialogProgress} from 'projects/dialog/src/lib/default-dialogs/wait-dialog/wait-dialog-progress';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DefaultDialogService extends DialogService {

  constructor(dialog: MatDialog) {
    super(dialog);
  }

  public delete(name: string, items: string[], force = false, helpPageId?: HelpPageId): Observable<void> {
    if (force) {
      return of(null);
    }
    return this.open(DeleteDialogComponent, DialogSize.SIZE_MD, {
      name,
      items,
      helpPageId,
    });
  }

  public confirm(title: string, message: string, force = false): Observable<void> {
    if (force) {
      return of(null);
    }
    return this.open(ConfirmDialogComponent, DialogSize.SIZE_MD, {
      title,
      message,
    });
  }

  public wait(progress: WaitDialogProgress): MatDialogRef<WaitDialogComponent, void> {
    return this.dialog.open(WaitDialogComponent, {
      panelClass: DialogSize.SIZE_MD,
      data: progress,
      disableClose: true,
    });
  }

  public waitFor<T>(operation: Observable<T>, title = 'Please wait...'): Observable<T> {
    const dialogRef = this.wait({title, progress: -1});
    return operation.pipe(tap(x => dialogRef.close()));
  }
}
