import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {map} from 'rxjs/operators';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import * as _ from 'lodash';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {Container} from 'projects/runtime/src/lib/entities/container';
import {LOGS_ICON} from 'projects/icon/src/lib/icons';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@Component({
  selector: 'lib-containers-table',
  templateUrl: './containers-table.component.html',
  styleUrls: ['./containers-table.component.scss']
})
export class ContainersTableComponent implements OnInit, OnDestroy {

  private _subscriptions: Subscription[] = [];
  readonly displayedColumns: string[] = [/*'id',*/ 'startDate', 'hostId', 'status', 'label', 'name', 'logs'];
  readonly logsIcon = LOGS_ICON;
  dataSource: MatTableDataSource<Container>;
  private taskId: string;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private containerService: RuntimeContainerService,
              private eventBus: EventBusService,
              private hosts: RuntimeHostService) {
    this._subscriptions.push(eventBus.of<TaskSelectedEvent>(TaskSelectedEvent.CHANNEL)
      .pipe(map(event => event.task)).subscribe(task => {
        if (task) {
          this.taskId = task.id;
          this.containers = task.containers;
        } else {
          this.taskId = null;
          this.containers = [];
        }
      }));
  }

  ngOnInit() {
    this.hosts.hosts().subscribe();
  }

  ngOnDestroy() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  set containers(containers: Container[]) {
    this.dataSource = new MatTableDataSource(containers);
    this.dataSource.sort = this.sort;
  }

  logs(container: Container) {
    this.containerService.attachLogs(this.taskId, container).subscribe();
  }

}
