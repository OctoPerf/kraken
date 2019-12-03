import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SelectionModel} from '@angular/cdk/collections';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {REFRESH_ICON} from 'projects/icon/src/lib/icons';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Subscription} from 'rxjs';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {map} from 'rxjs/operators';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import * as _ from 'lodash';

@Component({
  selector: 'lib-tasks-table',
  templateUrl: './tasks-table.component.html',
  styleUrls: ['./tasks-table.component.scss']
})
export class TasksTableComponent implements OnInit, OnDestroy {

  readonly _selection: SelectionModel<Task> = new SelectionModel(false);
  private _subscriptions: Subscription[] = [];
  readonly refreshIcon = REFRESH_ICON;
  readonly displayedColumns: string[] = [/*'id',*/ 'startDate', 'status', 'type', 'description', 'containers'];
  loading = true;
  count = 0;
  dataSource: MatTableDataSource<Task>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private taskService: RuntimeTaskService,
              private eventBus: EventBusService) {
    this._subscriptions.push(eventBus.of<TasksRefreshEvent>(TasksRefreshEvent.CHANNEL)
      .pipe(map(event => event.tasks)).subscribe(tasks => this.tasks = tasks));
    this._subscriptions.push(this._selection.changed.subscribe(value => {
      this.eventBus.publish(new TaskSelectedEvent(this._selection.selected[0]));
    }));
  }

  ngOnInit() {
    this.refresh();
  }

  ngOnDestroy() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  refresh() {
    this.loading = true;
    this.taskService.list().subscribe();
  }

  set tasks(tasks: Task[]) {
    this.count = tasks.length;
    this.dataSource = new MatTableDataSource(tasks);
    this.dataSource.sort = this.sort;
    this.loading = false;
  }

}
