import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';

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
        this.formGroup.addControl('applicationId', new FormControl(this._owner.applicationId, [Validators.required]));
        this.formGroup.removeControl('userId');
        break;
      case 'USER':
        this.formGroup.addControl('applicationId', new FormControl(this._owner.applicationId, [Validators.required]));
        this.formGroup.addControl('userId', new FormControl(this._owner.userId, [Validators.required]));
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
        return new Owner();
      case 'APPLICATION':
        return new Owner('', '', this.applicationId.value, 'APPLICATION');
      case 'USER':
        return new Owner(this.userId.value, '', this.applicationId.value, 'USER');
    }
  }
}
