import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {of} from 'rxjs';
import {CommandService} from 'projects/command/src/lib/command.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventSourceSpy} from 'projects/tools/src/lib/event-source.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {Command} from 'projects/command/src/lib/entities/command';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {ExecuteCommandDialogComponent} from 'projects/command/src/lib/command-dialogs/execute-command-dialog/execute-command-dialog.component';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {CommandConfigurationService} from 'projects/command/src/lib/command-configuration.service';
import {commandConfigurationServiceSpy} from 'projects/command/src/lib/command-configuration.service.spec';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {retriesServiceSpy} from 'projects/tools/src/lib/retries.service.spec';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import SpyObj = jasmine.SpyObj;
import {QueryParamsToStringPipe} from 'projects/tools/src/lib/query-params-to-string.pipe';
import {EventEmitter} from '@angular/core';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

export const commandServiceSpy = () => {
  const spy = jasmine.createSpyObj('CommandService', [
    'executeCommand',
    'executeShell',
    'executeScript',
    'runCommand',
    'listen',
    'cancel',
    'setCommandLabel',
    'getCommandName',
    'getCommandTitle',
    'removeCommandLabel',
  ]);
  spy.commandLabelsChanged = new EventEmitter<void>();
  return spy;
};


describe('CommandService', () => {
  let service: CommandService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let eventSource: EventSource;
  let dialogs: SpyObj<DialogService>;
  let storage: SpyObj<LocalStorageService>;

  beforeEach(() => {
    eventSource = eventSourceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
        {provide: ConfigurationService, useValue: configurationServiceMock()},
        {provide: CommandConfigurationService, useValue: commandConfigurationServiceSpy()},
        {provide: RetriesService, useValue: retriesServiceSpy()},
        CommandService,
        DurationToStringPipe,
        QueryParamsToStringPipe,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    dialogs = TestBed.get(DialogService);
    storage = TestBed.get(LocalStorageService);
    service = TestBed.get(CommandService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should listen', () => {
    service.listen();
    expect(service._eventSourceSubscription).toBeTruthy();
    const subscription = service._eventSourceSubscription = jasmine.createSpyObj('_subscription', ['unsubscribe']);
    service.listen();
    expect(subscription.unsubscribe).toHaveBeenCalled();
  });

  it('should executeCommand', () => {
    const command = new Command(['test']);
    dialogs.open.and.returnValue(of(command));
    service.executeCommand(command).subscribe();
    const path = service.formatCommandPath(command.path);
    expect(dialogs.open).toHaveBeenCalledWith(ExecuteCommandDialogComponent, DialogSize.SIZE_MD, {command, path});
    const req = httpTestingController.expectOne(request => request.url === 'commandApiUrl/command/execute');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(new Command(['test'], {}, '', [], 'application'));
    req.flush('commandId');
    httpTestingController.verify();
  });

  it('should executeShell', () => {
    dialogs.open.and.returnValue(of(new Command(['test'])));
    service.executeShell('path').subscribe();
    const command = new Command([Command.SHELL_0, Command.SHELL_1, 'echo $KEY'], {KEY: 'value'});
    const path = service.formatCommandPath('path');
    expect(dialogs.open).toHaveBeenCalledWith(ExecuteCommandDialogComponent, DialogSize.SIZE_MD, {command, path});
    const req = httpTestingController.expectOne(request => request.url === 'commandApiUrl/command/execute');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(new Command(['test'], {}, 'path', [], 'application'));
    req.flush('commandId');
    httpTestingController.verify();
  });

  it('should execute script', () => {
    service.executeScript('path', 'name').subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'commandApiUrl/command/execute');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(
      new Command([
        '/bin/sh',
        '-c',
        `chmod +x name && ./name`
      ], {}, 'path', [], 'application')
    );
    req.flush('commandId');
    httpTestingController.verify();
  });

  it('should run command', () => {
    service.runCommand(new Command(['test'], {}, 'path')).subscribe();
    const req = httpTestingController.expectOne(request => request.url === 'commandApiUrl/command/execute');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(new Command(['test'], {}, 'path', [], 'application'));
    req.flush('commandId');
    httpTestingController.verify();
  });

  it('should cancel succeed', () => {
    service.cancel('commandId').subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'commandApiUrl/command/cancel');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('commandId')).toBe('commandId');
    request.flush('true');
  });

  it('should cancel fail', () => {
    service.cancel('commandId').subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'commandApiUrl/command/cancel');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('commandId')).toBe('commandId');
    request.flush('false');
    expect(eventBus.publish).toHaveBeenCalled();
  });

  it('should set and get name', () => {
    const command = new Command(['foo', 'bar'], {}, '', [], '', 'commandId');
    service.setCommandLabel(command.id, 'name', 'title');
    expect(service.getCommandName(command)).toBe('name');
    expect(service.getCommandTitle(command)).toBe('title');
    service.removeCommandLabel(command.id);
    expect(service.getCommandName(command)).toBe('foo bar');
    expect(service.getCommandTitle(command)).toBe('foo bar in root path');
  });

  it('should set and get name with path', () => {
    const command = new Command(['foo', 'bar'], {}, 'path', [], '', 'commandId');
    service.setCommandLabel(command.id, 'name', 'title');
    expect(service.getCommandName(command)).toBe('name');
    expect(service.getCommandTitle(command)).toBe('title');
    service.removeCommandLabel(command.id);
    expect(service.getCommandName(command)).toBe('foo bar');
    expect(service.getCommandTitle(command)).toBe('foo bar in path \'path\'');
  });

  it('should handle error', fakeAsync(() => {
    const listen = spyOn(service, 'listen');
    service._onError();
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(listen).toHaveBeenCalled();
  }));

  it('should not handle error destroyed', fakeAsync(() => {
    const listen = spyOn(service, 'listen');
    service.ngOnDestroy();
    service._onError();
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(listen).not.toHaveBeenCalled();
  }));

  it('should send event on message', () => {
    service._onMessage({command: new Command(['java', '--version']), status: 'INITIALIZED', text: ''});
    expect(service._retry.reset).toHaveBeenCalled();
    expect(eventBus.publish).toHaveBeenCalled();
  });
});


