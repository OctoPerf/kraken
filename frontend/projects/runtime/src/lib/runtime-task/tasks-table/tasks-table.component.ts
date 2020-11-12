import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {CLEAR_ICON, REFRESH_ICON, STOP_ICON} from 'projects/icon/src/lib/icons';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Subscription} from 'rxjs';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {map} from 'rxjs/operators';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import * as _ from 'lodash';
import {ContainerStatusIsTerminalPipe} from 'projects/runtime/src/lib/runtime-task/container-status/container-status-is-terminal.pipe';
import {MonoSelectionWrapper} from 'projects/components/src/lib/selection/mono-selection-wrapper';
import {isTerminalContainerStatus} from 'projects/runtime/src/lib/entities/container-status';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';

@Component({
  selector: 'lib-tasks-table',
  templateUrl: './tasks-table.component.html',
  styleUrls: ['./tasks-table.component.scss']
})
export class TasksTableComponent implements OnInit, OnDestroy {

  readonly ID = 'tasks';

  readonly _selection: MonoSelectionWrapper<Task> = new MonoSelectionWrapper<Task>(this._match.bind(this));
  private _subscriptions: Subscription[] = [];
  readonly refreshIcon = REFRESH_ICON;
  readonly stopIcon = STOP_ICON;
  readonly removeIcon = CLEAR_ICON;
  readonly displayedColumns: string[] = [/*'id',*/ 'startDate', 'status', 'type', 'description', 'containers', 'buttons'];
  loading = true;
  dataSource: MatTableDataSource<Task> = new MatTableDataSource<Task>([]);

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private taskService: RuntimeTaskService,
              private eventBus: EventBusService,
              private dialogs: DefaultDialogService,
              private statusIsTerminal: ContainerStatusIsTerminalPipe) {
    this._subscriptions.push(eventBus.of<TasksRefreshEvent>(TasksRefreshEvent.CHANNEL)
      .pipe(map(event => event.tasks)).subscribe(tasks => this.tasks = tasks));
    this._subscriptions.push(this._selection.model.changed.subscribe(value => {
      this.eventBus.publish(new TaskSelectedEvent(this._selection.selection));
    }));
  }

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.refresh();
  }

  ngOnDestroy() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  refresh() {
    this.loading = true;
    this.taskService.list().subscribe();
  }

  _match(t1: Task, t2: Task): boolean {
    return t1.id === t2.id;
  }

  public cancel(task: Task, force = false) {
    this.dialogs.confirm('Cancel Task', 'Are you sure you want to cancel this task execution?', force)
      .subscribe(() => {
        this.loading = true;
        this.taskService.cancel(task).subscribe();
      });
  }

  public remove(task: Task) {
    this.loading = true;
    this.taskService.remove(task).subscribe();
  }

  public deleteSelection(force: boolean): boolean {
    const task = this._selection.selection;
    if (this.statusIsTerminal.transform(task.status)) {
      this.remove(task);
    } else {
      this.cancel(task, force);
    }
    return true;
  }

  set tasks(tasks: Task[]) {
    const taskId = this._selection.hasSelection ? this._selection.selection.id : null;
    const first = _.first(_.filter(tasks, (task: Task) => !isTerminalContainerStatus(task.status)));
    const currentNotTerminal = _.find(tasks, (task: Task) => task.id === taskId && !isTerminalContainerStatus(task.status));
    const current = _.find(tasks, {id: taskId});
    this._selection.selection = currentNotTerminal || first || current;
    this.dataSource.data = tasks;
    this.loading = false;
  }

}
