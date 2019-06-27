import {Injectable, TemplateRef} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ComponentType} from '@angular/cdk/portal';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {InspectDialogComponent} from 'projects/dialog/src/lib/inspect-dialog/inspect-dialog.component';
import {DeleteDialogComponent} from 'projects/dialog/src/lib/delete-dialog/delete-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/logs-dialog/logs-dialog.component';
import {WaitDialogProgress} from 'projects/dialog/src/lib/wait-dialog/wait-dialog-progress';
import {WaitDialogComponent} from 'projects/dialog/src/lib/wait-dialog/wait-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

@Injectable({
  providedIn: 'root'
})
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

  public delete(name: string, items: string[], helpPageId?: HelpPageId): Observable<void> {
    return this.open(DeleteDialogComponent, DialogSize.SIZE_MD, {
      name,
      items,
      helpPageId,
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
