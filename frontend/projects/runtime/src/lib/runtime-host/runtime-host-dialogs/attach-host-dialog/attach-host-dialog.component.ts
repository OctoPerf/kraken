import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Host} from 'projects/runtime/src/lib/entities/host';
import * as _ from 'lodash';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {OwnerSelectorComponent} from 'projects/security/src/lib/owner-selector/owner-selector.component';

export interface AttachHostDialogData {
  title: string;
  host: Host;
}

@Component({
  selector: 'lib-attach-host-dialog',
  templateUrl: './attach-host-dialog.component.html'
})
export class AttachHostDialogComponent {

  hostForm: FormGroup;

  @ViewChild('ownerSelector', {static: true})
  ownerSelector: OwnerSelectorComponent;

  constructor(public dialogRef: MatDialogRef<AttachHostDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: AttachHostDialogData,
              private fb: FormBuilder) {
    this.hostForm = this.fb.group({
      hostId: [data.host.id, [
        Validators.required,
        Validators.maxLength(63),
        Validators.minLength(4),
        Validators.pattern(/^[a-z0-9]+[a-z0-9\-]*[a-z0-9]+$/),
      ]],
    });
  }

  get hostId() {
    return this.hostForm.get('hostId');
  }

  attach() {
    const host = _.cloneDeep(this.data.host);
    host.id = this.hostId.value;
    host.owner = this.ownerSelector.owner;
    this.dialogRef.close(host);
  }

}
