import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SelectionModel} from '@angular/cdk/collections';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {REFRESH_ICON, STOP_ICON} from 'projects/icon/src/lib/icons';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Subscription} from 'rxjs';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {flatMap, map, tap} from 'rxjs/operators';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import * as _ from 'lodash';
import {TaskCancelledEvent} from 'projects/runtime/src/lib/events/task-cancelled-event';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';

@Component({
  selector: 'lib-tasks-table',
  templateUrl: './tasks-table.component.html',
  styleUrls: ['./tasks-table.component.scss']
})
export class TasksTableComponent implements OnInit, OnDestroy {

  readonly _selection: SelectionModel<Task> = new SelectionModel(false);
  private _subscriptions: Subscription[] = [];
  readonly refreshIcon = REFRESH_ICON;
  readonly stopIcon = STOP_ICON;
  readonly displayedColumns: string[] = [/*'id',*/ 'startDate', 'status', 'type', 'description', 'containers', 'buttons'];
  loading = true;
  dataSource: MatTableDataSource<Task>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private taskService: RuntimeTaskService,
              private eventBus: EventBusService,
              private dialogs: DialogService) {
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

  public isSelected(task: Task): boolean {
    return this.hasSelection && this.selection.id === task.id;
  }

  public get hasSelection(): boolean {
    return this._selection.hasValue();
  }

  public set selection(task: Task) {
    if (task) {
      this._selection.select(task);
    } else {
      this._selection.clear();
    }
  }

  public get selection(): Task | null {
    return this.hasSelection ? this._selection.selected[0] : null;
  }

  public cancel(task: Task) {
    this.dialogs.confirm('Cancel Task', 'Are you sure you want to cancel this task execution?')
      .subscribe(() => this.taskService.cancel(task.id, task.type).subscribe());
  }

  set tasks(tasks: Task[]) {
    const taskId = this.hasSelection ? this.selection.id : null;
    const first = _.first(_.filter(tasks, (task: Task) => task.status !== 'DONE'));
    const currentNotDone = _.find(tasks, (task: Task) => task.id === taskId && task.status !== 'DONE');
    const current = _.find(tasks, {id: taskId});
    this.selection = currentNotDone || first || current;
    this.dataSource = new MatTableDataSource(tasks);
    this.dataSource.sort = this.sort;
    this.loading = false;
  }

}
