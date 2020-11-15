import {Component, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectNameInputComponent} from 'projects/project/src/app/project/project-name-input/project-name-input.component';
import {ApplicationInputComponent} from 'projects/project/src/app/project/application-input/application-input.component';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent {

  projectForm: FormGroup;

  @ViewChild(ProjectNameInputComponent, {static: true})
  projectName: ProjectNameInputComponent;

  @ViewChild(ApplicationInputComponent, {static: true})
  applicationId: ApplicationInputComponent;

  constructor(private fb: FormBuilder,
              private projectService: ProjectService,
              private router: Router,
              private route: ActivatedRoute) {
    this.projectForm = this.fb.group({});
  }

  create() {
    this.projectService.createProject(this.projectName.projectName.value, this.applicationId.applicationId.value).subscribe(value => this.router.navigate(['..'], {relativeTo: this.route}));
  }
}
