import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {Project} from 'projects/project/src/app/project/entities/project';
import {INSPECT_ICON} from 'projects/icon/src/lib/icons';
import * as _ from 'lodash';
import {BehaviorSubject, Subscription} from 'rxjs';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit, OnDestroy {

  readonly searchIcon = INSPECT_ICON;

  public filteredProjects: BehaviorSubject<Project[]>;
  public filter = '';

  private subscription: Subscription;

  constructor(private projectService: ProjectService) {
    this.filteredProjects = new BehaviorSubject([]);
  }

  ngOnInit(): void {
    this.subscription = this.projectService.projectsSubject.subscribe(_projects => this.projects = _projects);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  filterChanged() {
    this.projects = this.projectService.projectsSubject.getValue();
  }

  set projects(projects: Project[]) {
    let filtered: Project[] = projects;
    if (this.filter) {
      const lowerCaseFilter = this.filter.toLowerCase();
      filtered = _.filter(projects, project => project.name.toLowerCase().indexOf(lowerCaseFilter) > -1);
    }
    this.filteredProjects.next(_.sortBy(filtered, project => -project.updateDate));
  }

  get projects(): Project[] {
    return this.filteredProjects.getValue();
  }
}
