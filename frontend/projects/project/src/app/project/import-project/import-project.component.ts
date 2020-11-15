import {Component, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ProjectNameInputComponent} from 'projects/project/src/app/project/project-name-input/project-name-input.component';
import {ApplicationInputComponent} from 'projects/project/src/app/project/application-input/application-input.component';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {ActivatedRoute, Router} from '@angular/router';
import {RepositoryUrlInputComponent} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';

@Component({
  selector: 'app-import-project',
  templateUrl: './import-project.component.html',
  styleUrls: ['./import-project.component.scss']
})
export class ImportProjectComponent {

  projectForm: FormGroup;

  @ViewChild(ProjectNameInputComponent, {static: true})
  projectName: ProjectNameInputComponent;

  @ViewChild(ApplicationInputComponent, {static: true})
  applicationId: ApplicationInputComponent;

  @ViewChild(RepositoryUrlInputComponent, {static: true})
  repositoryUrl: RepositoryUrlInputComponent;

  constructor(private fb: FormBuilder,
              private projectService: ProjectService,
              private router: Router,
              private route: ActivatedRoute,
              private dialogs: DefaultDialogService) {
    this.projectForm = this.fb.group({});
  }

  create() {
    this.dialogs.waitFor(this.projectService.importFromGit(
      this.projectName.projectName.value,
      this.applicationId.applicationId.value,
      this.repositoryUrl.repositoryUrl.value
    )).subscribe(value => this.router.navigate(['..'], {relativeTo: this.route}));
  }

}
