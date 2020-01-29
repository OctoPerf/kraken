import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-description-input',
  templateUrl: './description-input.component.html',
  styleUrls: ['./description-input.component.scss']
})
export class DescriptionInputComponent implements OnInit {

  @Input() formGroup: FormGroup;
  @Input() value: string;

  ngOnInit() {
    this.formGroup.addControl('description', new FormControl(this.value, [Validators.required, Validators.pattern(/^[\w\s]+$/)]));
  }

  get description() {
    return this.formGroup.get('description');
  }
}
