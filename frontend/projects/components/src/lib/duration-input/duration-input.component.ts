import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';

@Component({
  selector: 'lib-duration-input',
  templateUrl: './duration-input.component.html',
  styleUrls: ['./duration-input.component.scss']
})
export class DurationInputComponent implements OnInit {

  private static readonly ID_PREFIX = 'duration-input';

  @Input() formGroup: FormGroup;
  @Input() id: string;
  @Input() storageId: string;
  @Input() label: string;

  private durationId: string;
  private unitId: string;
  private durationStorageId: string;
  private unitStorageId: string;

  constructor(private fb: FormBuilder,
              private localStorage: LocalStorageService) {
  }

  ngOnInit(): void {
    this.durationId = this.id + '-duration';
    this.unitId = this.id + '-unit';
    this.durationStorageId = DurationInputComponent.ID_PREFIX + 'value-' + this.id;
    this.unitStorageId = DurationInputComponent.ID_PREFIX + 'unit-' + this.id;
    const duration = this.localStorage.getNumber(this.durationStorageId, 5);
    const unit = this.localStorage.getString(this.unitStorageId, 'M');
    this.formGroup.addControl(this.durationId, new FormControl(duration, [
      Validators.required,
      Validators.min(0),
    ]));
    this.formGroup.addControl(this.unitId, new FormControl(unit, [
      Validators.required,
    ]));
  }

  get duration() {
    return this.formGroup.get(this.durationId);
  }

  get unit() {
    return this.formGroup.get(this.unitId);
  }

  get asString(): string {
    this.localStorage.set(this.durationStorageId, this.duration.value);
    this.localStorage.set(this.unitStorageId, this.unit.value);
    return `PT${this.duration.value}${this.unit.value}`;
  }
}
