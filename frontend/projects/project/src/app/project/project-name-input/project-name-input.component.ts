import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-project-name-input',
  templateUrl: './project-name-input.component.html',
  styleUrls: ['./project-name-input.component.scss']
})
export class ProjectNameInputComponent implements OnInit {

  @Input()
  projectForm: FormGroup;

  ngOnInit(): void {
    this.projectForm.addControl('projectName', new FormControl('', [
      Validators.required,
      Validators.maxLength(63),
      Validators.minLength(4),
    ]));
  }

  get projectName() {
    return this.projectForm.get('projectName');
  }
}
