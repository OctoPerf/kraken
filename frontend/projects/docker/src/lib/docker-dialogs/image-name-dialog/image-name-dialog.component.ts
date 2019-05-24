import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'lib-image-name-dialog',
  templateUrl: './image-name-dialog.component.html',
  styleUrls: ['./image-name-dialog.component.scss']
})
export class ImageNameDialogComponent {

  pullForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<ImageNameDialogComponent>,
              private fb: FormBuilder) {
    this.pullForm = this.fb.group({
      image: ['', [Validators.required]],
    });
  }

  get image() {
    return this.pullForm.get('image');
  }
}
