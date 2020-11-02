import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ProjectNameInputComponent} from 'projects/project/src/app/project/project-name-input/project-name-input.component';
import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-application-input',
  templateUrl: './application-input.component.html',
  styleUrls: ['./application-input.component.scss']
})
export class ApplicationInputComponent implements OnInit {

  @Input()
  projectForm: FormGroup;
  applications: string[];

  constructor(private configuration: ProjectConfigurationService) {
    this.applications = this.configuration.availableApplications();
  }

  ngOnInit(): void {
    this.projectForm.addControl('applicationId', new FormControl(this.applications[0], [
      Validators.required,
    ]));
  }

  get applicationId() {
    return this.projectForm.get('applicationId');
  }
}
