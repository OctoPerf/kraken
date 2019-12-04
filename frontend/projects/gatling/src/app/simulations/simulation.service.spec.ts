import {TestBed} from '@angular/core/testing';

import {SimulationService} from './simulation.service';
import {StorageNodeToExtPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-ext.pipe';
import {testStorageDirectoryNode, testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {of} from 'rxjs';
import {ExecuteSimulationDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/execute-simulation-dialog/execute-simulation-dialog.component';
import {DateTimeToStringPipe} from 'projects/date/src/lib/date-time-to-string.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {analysisServiceSpy} from 'projects/analysis/src/lib/analysis.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {gatlingConfigurationServiceSpy} from 'projects/gatling/src/app/gatling-configuration.service.spec';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {ImportHarDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/import-har-dialog/import-har-dialog.component';
import {OpenResultsEvent} from 'projects/analysis/src/lib/events/open-results-event';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import SpyObj = jasmine.SpyObj;
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {runtimeTaskServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-task.service.spec';
import {testExecutionContext} from 'projects/runtime/src/lib/entities/execution-context.spec';
import {OpenTasksEvent} from 'projects/runtime/src/lib/events/open-tasks-event';

export const simulationServiceSpy = () => {
  const spy = jasmine.createSpyObj('SimulationService', [
    'run',
    'debug',
    'isSimulationNode',
    'isHarNode',
  ]);
  return spy;
};

describe('SimulationService', () => {
  let service: SimulationService;
  let storage: SpyObj<StorageService>;
  let dialogs: SpyObj<DialogService>;
  let eventBus: SpyObj<EventBusService>;
  let tasks: SpyObj<RuntimeTaskService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        {provide: RuntimeTaskService, useValue: runtimeTaskServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: GatlingConfigurationService, useValue: gatlingConfigurationServiceSpy()},
        StorageNodeToExtPipe,
        DateTimeToStringPipe,
        SimulationService,
      ]
    });
    service = TestBed.get(SimulationService);
    tasks = TestBed.get(RuntimeTaskService);
    storage = TestBed.get(StorageService);
    dialogs = TestBed.get(DialogService);
    eventBus = TestBed.get(EventBusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should run', () => {
    storage.getContent.and.returnValue(of(`
package computerdatabase.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

class AdvancedSimulationStep05 extends Simulation {

object Search {
    `));
    const context = testExecutionContext();
    dialogs.open.and.returnValue(of(context));
    tasks.execute.and.returnValue(of('taskId'));
    service.run(testStorageFileNode());
    expect(dialogs.open).toHaveBeenCalledWith(ExecuteSimulationDialogComponent,
      DialogSize.SIZE_LG, {
        simulationPackage: 'computerdatabase.advanced',
        simulationClass: 'AdvancedSimulationStep05',
        type: 'RUN',
        atOnce: false,
      });
    expect(tasks.execute).toHaveBeenCalledWith(context);
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenResultsEvent());
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenTasksEvent());
  });

  it('should debug', () => {
    storage.getContent.and.returnValue(of(`
package computerdatabase.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

class AdvancedSimulationStep05 extends Simulation {

setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))

object Search {
    `));
    const context = testExecutionContext();
    dialogs.open.and.returnValue(of(context));
    tasks.execute.and.returnValue(of('taskId'));
    service.debug(testStorageFileNode());
    expect(dialogs.open).toHaveBeenCalledWith(ExecuteSimulationDialogComponent,
      DialogSize.SIZE_LG, {
        simulationPackage: 'computerdatabase.advanced',
        simulationClass: 'AdvancedSimulationStep05',
        type: 'DEBUG',
        atOnce: true,
      });
    expect(tasks.execute).toHaveBeenCalledWith(context);
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenResultsEvent());
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenTasksEvent());
  });

  it('should return isSimulationNode', () => {
    expect(service.isSimulationNode(null)).toBeFalsy();
    expect(service.isSimulationNode(testStorageDirectoryNode())).toBeFalsy();
    expect(service.isSimulationNode(testStorageFileNode())).toBeFalsy();
    expect(service.isSimulationNode({
      path: 'simulations/basic.scala',
      type: 'FILE',
      depth: 1,
      length: 42,
      lastModified: 1337
    })).toBeTruthy();
  });

  it('should return isHarNode', () => {
    expect(service.isHarNode(null)).toBeFalsy();
    expect(service.isHarNode(testStorageDirectoryNode())).toBeFalsy();
    expect(service.isHarNode(testStorageFileNode())).toBeFalsy();
    expect(service.isHarNode({
      path: 'simulations/basic.har',
      type: 'FILE',
      depth: 1,
      length: 42,
      lastModified: 1337
    })).toBeTruthy();
  });

  it('should uploadHar', () => {
    const importHar = spyOn(service, 'importHar');
    dialogs.open.and.returnValue(of(['test.har']));
    service.uploadHar();
    expect(dialogs.open).toHaveBeenCalledWith(FileUploadDialogComponent,
      DialogSize.SIZE_MD, {
        endpoint: 'storageApiUrl/files/set/file?path=gatling/user-files/simulations',
        multiple: false,
        accept: '.har',
        title: 'Upload HAR File'
      });
    expect(importHar).toHaveBeenCalledWith({path: 'gatling/user-files/simulations/test.har'});
  });

  it('should importHar', () => {
    const context = testExecutionContext();
    dialogs.open.and.returnValue(of(context));
    tasks.execute.and.returnValue(of('taskId'));
    service.importHar({path: 'gatling/user-files/simulations/test.har'} as any);
    expect(dialogs.open).toHaveBeenCalledWith(ImportHarDialogComponent, DialogSize.SIZE_MD, {harPath: 'test.har'});
    expect(tasks.execute).toHaveBeenCalledWith(context);
  });

});


