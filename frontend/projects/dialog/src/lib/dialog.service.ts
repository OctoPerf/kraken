import {Injectable, TemplateRef} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ComponentType} from '@angular/cdk/portal';
import {Observable, of} from 'rxjs';
import {filter} from 'rxjs/operators';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {InspectDialogComponent} from 'projects/dialog/src/lib/default-dialogs/inspect-dialog/inspect-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/default-dialogs/logs-dialog/logs-dialog.component';
import {ConfirmDialogComponent} from 'projects/dialog/src/lib/default-dialogs/confirm-dialog/confirm-dialog.component';
import {DeleteDialogComponent} from 'projects/dialog/src/lib/default-dialogs/delete-dialog/delete-dialog.component';
import {WaitDialogComponent} from 'projects/dialog/src/lib/default-dialogs/wait-dialog/wait-dialog.component';
import {WaitDialogProgress} from 'projects/dialog/src/lib/default-dialogs/wait-dialog/wait-dialog-progress';

@Injectable()
export class DialogService {

  constructor(public dialog: MatDialog) {
  }

  public open<T, D = any, R = any>(componentOrTemplateRef: ComponentType<T> | TemplateRef<T>,
                                   size: DialogSize = DialogSize.SIZE_SM,
                                   data?: D): Observable<R> {
    const dialogRef = this.dialog.open(componentOrTemplateRef, {
      panelClass: size,
      data
    });
    return dialogRef.afterClosed().pipe(filter((result: R) => result !== undefined));
  }

  public inspect(name: string, object: string, helpPageId?: HelpPageId): MatDialogRef<InspectDialogComponent, void> {
    return this.dialog.open(InspectDialogComponent, {
      panelClass: DialogSize.SIZE_LG,
      data: {
        object,
        name,
        helpPageId,
      }
    });
  }

  public logs(title: string, logs: string): MatDialogRef<LogsDialogComponent, void> {
    return this.dialog.open(LogsDialogComponent, {
      panelClass: DialogSize.SIZE_FULL,
      data: {
        title,
        logs,
      }
    });
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
}
