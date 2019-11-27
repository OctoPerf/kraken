import {TestBed} from '@angular/core/testing';

import {RuntimeContainerService} from './runtime-container.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {testContainer} from 'projects/runtime/src/lib/entities/container.spec';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {LogsDetachedEvent} from 'projects/runtime/src/lib/events/logs-detached-event';

export const runtimeContainerServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeContainerService', [
    'attachLogs',
    'detachLogs',
  ]);
  return spy;
};

describe('RuntimeContainerService', () => {
  let service: RuntimeContainerService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeConfigurationService, useValue: runtimeConfigurationServiceSpy()},
        EventBusService,
        RuntimeContainerService,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(RuntimeContainerService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should attach logs', () => {
    const publish = spyOn(eventBus, 'publish');
    const taskId = 'taskId';
    const container = testContainer();
    const id = 'id';
    service.attachLogs(taskId, container).subscribe(data => expect(data).toBe(id), () => fail('attach failed'));
    const request = httpTestingController.expectOne(req => req.url === 'containerApiUrl/container/logs/attach');
    expect(request.request.method).toBe('POST');
    expect(request.request.params.get('taskId')).toBe(taskId);
    expect(request.request.params.get('containerId')).toBe(container.id);
    expect(request.request.params.get('containerName')).toBe(container.name);
    request.flush(id);
    expect(publish).toHaveBeenCalledWith(new LogsAttachedEvent(id, container));
  });

  it('should detach logs', () => {
    const publish = spyOn(eventBus, 'publish');
    const id = 'id';
    service.detachLogs(id).subscribe(data => expect(data).toBe(id), () => fail('attach failed'));
    const request = httpTestingController.expectOne(req => req.url === 'containerApiUrl/container/logs/detach');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('id')).toBe(id);
    request.flush(id);
    expect(publish).toHaveBeenCalledWith(new LogsDetachedEvent(id));
  });
});
