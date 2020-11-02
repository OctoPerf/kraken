import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ValidatorTools} from 'projects/tools/src/lib/validator-tools';

@Component({
  selector: 'lib-repository-url-input',
  templateUrl: './repository-url-input.component.html',
  styleUrls: ['./repository-url-input.component.scss']
})
export class RepositoryUrlInputComponent  implements OnInit {

  @Input()
  formGroup: FormGroup;

  ngOnInit(): void {
    this.formGroup.addControl('repositoryUrl', new FormControl('', [
      Validators.required,
      ValidatorTools.gitUrl(),
    ]));
  }

  get repositoryUrl() {
    return this.formGroup.get('repositoryUrl');
  }
}
