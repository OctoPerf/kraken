import {Component, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faSearch} from '@fortawesome/free-solid-svg-icons/faSearch';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {DockerContainer} from 'projects/docker/src/lib/entities/docker-container';
import {faFileAlt} from '@fortawesome/free-solid-svg-icons/faFileAlt';
import * as moment from 'moment';
import {Moment} from 'moment';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DOCKER_ID} from 'projects/docker/src/lib/docker-id';
import {DELETE_ICON, MENU_ICON, REFRESH_ICON} from 'projects/icon/src/lib/icons';
import * as _ from 'lodash';
import {faPlay} from '@fortawesome/free-solid-svg-icons/faPlay';
import {faPause} from '@fortawesome/free-solid-svg-icons/faPause';
import {faPlusCircle} from '@fortawesome/free-solid-svg-icons/faPlusCircle';
import {
  RunContainerData,
  RunContainerDialogComponent
} from 'projects/docker/src/lib/docker-dialogs/run-container-dialog/run-container-dialog.component';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {SystemPruneDialogComponent} from 'projects/docker/src/lib/docker-dialogs/system-prune-dialog/system-prune-dialog.component';
import {faBroom} from '@fortawesome/free-solid-svg-icons/faBroom';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {JsonPipe} from '@angular/common';


library.add(faFileAlt, faPlay, faPause, faPlusCircle, faBroom);

@Component({
  selector: 'lib-containers-table',
  templateUrl: './containers-table.component.html',
  styleUrls: ['./containers-table.component.scss']
})
export class ContainersTableComponent implements OnInit, OnDestroy {

  readonly logsIcon = new IconFa(faFileAlt);
  readonly runIcon = new IconFa(faPlusCircle, 'success');
  readonly pruneIcon = new IconFa(faBroom);
  readonly startIcon = new IconFa(faPlay, 'success');
  readonly stopIcon = new IconFa(faPause, 'warn');
  readonly menuIcon = MENU_ICON;
  readonly deleteIcon = DELETE_ICON;
  readonly refreshIcon = REFRESH_ICON;
  readonly fullIcon = new IconFa(faSearch);

  readonly displayedColumns: string[] = ['name', 'image', 'status', 'logs', 'full', 'rm'];
  loading = true;
  dataSource: MatTableDataSource<DockerContainer>;
  tooltips: { [key in string]: { date: Moment, text: string } } = {};

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  private subscription: Subscription;

  constructor(@Inject(DOCKER_ID) private id: string,
              private dockerService: DockerService,
              private dialogs: DialogService,
              private eventBus: EventBusService,
              private localStorage: LocalStorageService,
              private json: JsonPipe) {
    this.dataSource = new MatTableDataSource([]);
    this.subscription = this.dockerService.containersSubject.subscribe((containers) => this.containers = containers);
  }

  ngOnInit() {
    this.refresh();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refresh() {
    this.loading = true;
    this.dockerService.ps().subscribe();
  }

  full(container: DockerContainer) {
    this.dialogs.inspect('Docker Container', this.json.transform(container.full), 'ADMIN_INSPECT_CONTAINER');
  }

  rm(container: DockerContainer) {
    this.dialogs.delete('Docker Container', [`${container.image} (${container.name})`], 'ADMIN_DELETE_CONTAINER').subscribe(() => {
      this.dockerService.rm(container).subscribe();
    });
  }

  loadTail(container: DockerContainer) {
    const id = container.id;
    const tooltip = this.tooltips[id];
    if (tooltip && moment.duration(moment().diff(tooltip.date)).asSeconds() < 3) {
      // Only refresh the tooltip at most every 3 seconds
      return;
    }
    this.dockerService.tail(container).subscribe(text => this.tooltips[id] = {date: moment(), text});
  }

  tail(container: DockerContainer): string {
    const id = container.id;
    const tooltip = this.tooltips[id];
    return tooltip ? tooltip.text : 'Loading...';
  }

  logs(container: DockerContainer) {
    this.dockerService.logs(container).subscribe();
  }

  isUp(container: DockerContainer): boolean {
    return _.startsWith(container.status, 'Up');
  }

  start(container: DockerContainer) {
    this.dockerService.start(container).subscribe();
  }

  stop(container: DockerContainer) {
    this.dockerService.stop(container).subscribe();
  }

  run() {
    const nameId = this.id + '-run-container-name';
    const configId = this.id + '-run-container-config';
    this.dialogs.open(RunContainerDialogComponent, DialogSize.SIZE_MD, {
      name: this.localStorage.getString(nameId, ''),
      config: this.localStorage.getString(configId, 'Image: image-name'),
    }).subscribe((data: RunContainerData) => {
      this.localStorage.set(nameId, data.name);
      this.localStorage.set(configId, data.config);
      this.dockerService.run(data.name, data.config).subscribe();
    });
  }

  prune() {
    this.dialogs.open(SystemPruneDialogComponent, DialogSize.SIZE_MD).subscribe((data: { all: boolean, volumes: boolean }) => {
      this.dockerService.prune(data.all, data.volumes).subscribe();
    });
  }

  set containers(containers: DockerContainer[]) {
    this.dataSource = new MatTableDataSource(containers);
    this.dataSource.sort = this.sort;
    this.loading = false;
    this.tooltips = {};
  }
}
