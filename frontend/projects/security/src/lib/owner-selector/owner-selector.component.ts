import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';
import {ApplicationOwner} from 'projects/security/src/lib/entities/application-owner';
import {UserOwner} from 'projects/security/src/lib/entities/user-owner';
import {PublicOwner} from 'projects/security/src/lib/entities/public-owner';

@Component({
  selector: 'lib-owner-selector',
  templateUrl: './owner-selector.component.html',
  styleUrls: ['./owner-selector.component.scss']
})
export class OwnerSelectorComponent implements OnInit {

  @Input('owner') _owner: Owner;
  @Input() formGroup: FormGroup;

  constructor() {
  }

  ngOnInit(): void {
    this.formGroup.addControl('type', new FormControl(this._owner.type, [Validators.required]));
    this.typeSelected(this._owner.type);
  }

  typeSelected(type: OwnerType): void {
    switch (type) {
      case 'PUBLIC':
        this.formGroup.removeControl('applicationId');
        this.formGroup.removeControl('userId');
        break;
      case 'APPLICATION':
        const applicationOwner = this._owner as ApplicationOwner;
        this.formGroup.addControl('applicationId', new FormControl(applicationOwner.applicationId, [Validators.required]));
        this.formGroup.removeControl('userId');
        break;
      case 'USER':
        const userOwner = this._owner as UserOwner;
        this.formGroup.addControl('applicationId', new FormControl(userOwner.applicationId, [Validators.required]));
        this.formGroup.addControl('userId', new FormControl(userOwner.userId, [Validators.required]));
        break;
    }
  }

  get type(): AbstractControl {
    return this.formGroup.get('type');
  }

  get userId(): AbstractControl {
    return this.formGroup.get('userId');
  }

  get applicationId(): AbstractControl {
    return this.formGroup.get('applicationId');
  }

  public get owner(): Owner {
    switch (this.type.value) {
      case 'PUBLIC':
        return new PublicOwner();
      case 'APPLICATION':
        return new ApplicationOwner(this.applicationId.value);
      case 'USER':
        return new UserOwner(this.applicationId.value, this.userId.value);
    }
  }
}
