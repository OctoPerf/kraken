import {Component, OnInit} from '@angular/core';
import {CurrentProjectService} from 'projects/commons/src/lib/current-project/current-project.service';

@Component({
  selector: 'lib-current-project',
  templateUrl: './current-project.component.html',
  styleUrls: ['./current-project.component.scss']
})
export class CurrentProjectComponent {

  constructor(public currentProjectService: CurrentProjectService) {
  }

}
