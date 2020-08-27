import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent {

  projectForm: FormGroup;
  applications: string[];

  constructor(private fb: FormBuilder,
              private configuration: ProjectConfigurationService,
              private projectService: ProjectService,
              private router: Router,
              private route: ActivatedRoute) {
    this.applications = this.configuration.availableApplications();
    this.projectForm = this.fb.group({
      projectName: ['', [
        Validators.required,
        Validators.maxLength(63),
        Validators.minLength(4),
      ]],
      applicationId: [this.applications[0], [
        Validators.required,
      ]],
    });
  }

  get projectName() {
    return this.projectForm.get('projectName');
  }

  get applicationId() {
    return this.projectForm.get('applicationId');
  }

  create() {
    this.projectService.createProject(this.projectName.value, this.applicationId.value).subscribe(value => this.router.navigate(['..'], {relativeTo: this.route}));
  }
}
