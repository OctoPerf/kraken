import {Component, OnInit, ViewChild} from '@angular/core';
import {Portal} from '@angular/cdk/portal';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {faQuestionCircle} from '@fortawesome/free-regular-svg-icons/faQuestionCircle';
import {library} from '@fortawesome/fontawesome-svg-core';
import {transition, trigger, useAnimation} from '@angular/animations';
import {fadeInAnimation} from 'projects/commons/src/lib/animations';
import {faFolder} from '@fortawesome/free-regular-svg-icons/faFolder';
import {faFolderOpen} from '@fortawesome/free-regular-svg-icons/faFolderOpen';
import {faServer} from '@fortawesome/free-solid-svg-icons/faServer';
import {HELP_ICON} from 'projects/icon/src/lib/icons';
import {SplitPane} from 'projects/split/src/lib/split-pane';

library.add(faFolder, faFolderOpen, faQuestionCircle, faBell, faServer);

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.scss'],
  animations: [
    trigger('insertWorkspace', [
      transition(':enter', useAnimation(fadeInAnimation, {params: {duration: '1s'}}))
    ]),
  ],
})
export class WorkspaceComponent implements OnInit {

  readonly helpIcon = HELP_ICON;

  public splits: SplitPane[];
  @ViewChild('projectsPortal', {static: true}) projectsPortal: Portal<any>;
  @ViewChild('welcomePortal', {static: true}) welcomePortal: Portal<any>;

  ngOnInit() {
    this.splits = [new SplitPane(this.projectsPortal, 20, 10), new SplitPane(this.welcomePortal, 80, 50)];
  }

}
