import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {tap} from 'rxjs/operators';
import {Container} from 'projects/runtime/src/lib/entities/container';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {LogsDetachedEvent} from 'projects/runtime/src/lib/events/logs-detached-event';

@Injectable()
export class RuntimeContainerService {

  constructor(private http: HttpClient,
              private runtimeConfiguration: RuntimeConfigurationService,
              private eventBus: EventBusService,
  ) {
  }

  public attachLogs(taskId: string, container: Container): Observable<string> {
    return this.http.post(this.runtimeConfiguration.containerApiUrl('/logs/attach'),
      null,
      {
        responseType: 'text',
        params: {
          taskId,
          containerId: container.id,
          containerName: container.name,
        }
      }).pipe(tap(id => this.eventBus.publish(new LogsAttachedEvent(id, container))));
  }

  public detachLogs(id: string): Observable<string> {
    return this.http.delete(this.runtimeConfiguration.containerApiUrl('/logs/detach'),
      {
        responseType: 'text',
        params: {
          id
        }
      }).pipe(tap(_id => this.eventBus.publish(new LogsDetachedEvent(_id))));
  }

}
