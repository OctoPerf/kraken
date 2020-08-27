import {Component, Input, OnInit} from '@angular/core';
import {Project} from 'projects/project/src/app/project/entities/project';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {DELETE_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'app-project-item',
  templateUrl: './project-item.component.html',
  styleUrls: ['./project-item.component.scss']
})
export class ProjectItemComponent {

  readonly deleteIcon = DELETE_ICON;

  hover = false;
  private _project: Project;

  constructor(private projectService: ProjectService) {
  }

  @Input() set project(project: Project) {
    this._project = project;
    this.hover = false;
  }

  get project(): Project {
    return this._project;
  }

  public delete(force: boolean) {
    this.projectService.deleteProject(this.project, force).subscribe();
  }

  public open() {
    this.projectService.updateProject(this.project).subscribe(project => this.projectService.openProject(project.applicationId, project.id));
  }
}
