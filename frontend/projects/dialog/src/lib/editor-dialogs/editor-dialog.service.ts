import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {InspectDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/inspect-dialog/inspect-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/logs-dialog/logs-dialog.component';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';

@Injectable({
  providedIn: 'root'
})
export class EditorDialogService extends DialogService {

  constructor(dialog: MatDialog) {
    super(dialog);
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

}
