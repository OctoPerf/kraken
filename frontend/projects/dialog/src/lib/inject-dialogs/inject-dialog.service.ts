import {Injectable} from '@angular/core';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {MatDialog} from '@angular/material/dialog';


/**
 * Dialog service that is not provided in the root scope and allows to open dialogs with injected services/pipes/... that are not available in the root scope.
 */
@Injectable()
export class InjectDialogService extends DialogService {

  constructor(dialog: MatDialog) {
    super(dialog);
  }

}
