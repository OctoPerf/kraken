import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {BehaviorSubject, Observable, Subscription} from 'rxjs';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {map, tap} from 'rxjs/operators';
import * as _ from 'lodash';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {TaskCancelledEvent} from 'projects/runtime/src/lib/events/task-cancelled-event';

@Injectable()
export class RuntimeTaskService implements OnDestroy {

  public tasksSubject: BehaviorSubject<Task[]>;
  private subscriptions: Subscription[] = [];

  constructor(private http: HttpClient,
              private runtimeConfiguration: RuntimeConfigurationService,
              private eventBus: EventBusService,
  ) {
    this.tasksSubject = new BehaviorSubject([]);

    this.subscriptions.push(
      this.eventBus.of<TasksRefreshEvent>(TasksRefreshEvent.CHANNEL)
        .pipe(map(event => event.tasks))
        .subscribe(data => this.tasksSubject.next(data))
    );
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  list(): Observable<Task[]> {
    return this.http.get<Task[]>(this.runtimeConfiguration.taskApiUrl('/list'))
      .pipe(tap(data => this.eventBus.publish(new TasksRefreshEvent(data))));
  }

  cancel(taskId: string, taskType: TaskType): Observable<string> {
    return this.http.delete(this.runtimeConfiguration.taskApiUrl(`/cancel/${taskType}`),
      {
        responseType: 'text',
        params: {taskId}
      }).pipe(tap(id => this.eventBus.publish(new TaskCancelledEvent(id))));
  }

  execute(context: ExecutionContext): Observable<string> {
    return this.http.post(this.runtimeConfiguration.taskApiUrl(),
      context,
      {
        responseType: 'text',
      }).pipe(tap(id => this.eventBus.publish(new TaskExecutedEvent(id, context))));
  }
}
