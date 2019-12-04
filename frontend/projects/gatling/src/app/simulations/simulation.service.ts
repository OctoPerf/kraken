import {Injectable} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeToExtPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-ext.pipe';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {filter, map} from 'rxjs/operators';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {DateTimeToStringPipe} from 'projects/date/src/lib/date-time-to-string.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {
  ExecuteSimulationDialogComponent,
  ExecuteSimulationDialogData
} from 'projects/gatling/src/app/simulations/simulation-dialogs/execute-simulation-dialog/execute-simulation-dialog.component';

@Injectable()
export class SimulationService {

  constructor(private toExt: StorageNodeToExtPipe,
              private storage: StorageService,
              private dialogs: DialogService,
              // private commands: CommandService,
              private analysis: AnalysisService,
              private dateToString: DateTimeToStringPipe,
              private eventBus: EventBusService,
              private storageConfiguration: StorageConfigurationService,
              private gatlingConfiguration: GatlingConfigurationService) {
  }

  run(node: StorageNode) {
    this._start(node, 'RUN');
  }

  debug(node: StorageNode) {
    this._start(node, 'DEBUG');
  }

  private _start(node: StorageNode,
                 type: TaskType) {
    const packageRegexp = /^\s*package\s+(\S+)\s*\n/gm;
    const classRegexp = /^\s*class\s+(\S+)\s+extends\s+Simulation/gm;
    const injectRegExp = /\.inject\(atOnceUsers\(1\)\)/gm;

    this.storage.getContent(node)
      .pipe(map((content) => {
        const simulationPackageExec = packageRegexp.exec(content);
        const simulationPackage = simulationPackageExec ? simulationPackageExec[1] : '';
        const simulationClassExec = classRegexp.exec(content);
        const simulationClass = simulationClassExec ? simulationClassExec[1] : '';
        const atOnce = content.search(injectRegExp) > -1;
        return {type, simulationPackage, simulationClass, atOnce};

        // type: TaskType;
        // description: string;
        // environment: { [key in string]: string };
      }))
      .subscribe((data: ExecuteSimulationDialogData) => {
        this.dialogs.open(ExecuteSimulationDialogComponent, DialogSize.SIZE_LG, data).subscribe(
          (result: { simulationName: string, description: string, javaOpts: string }) => {
            // endpoint(result.description, {
            //   GATLING_SIMULATION: result.simulationName,
            //   GATLING_RUN_DESCRIPTION: result.description,
            //   JAVA_OPTS: result.javaOpts,
            // }).subscribe((commandId: string) => {
            //   this.commands.setCommandLabel(commandId, result.description,
            //     result.simulationName + ' at ' + this.dateToString.transform(new Date()));
            //   this.eventBus.publish(new OpenResultsEvent());
            // });
          });
      });
  }

  isSimulationNode(node: StorageNode) {
    return node && node.type === 'FILE' && this.toExt.transform(node) === 'scala';
  }

  isHarNode(node: StorageNode) {
    return node && node.type === 'FILE' && this.toExt.transform(node) === 'har';
  }

  uploadHar() {
    const path = this.gatlingConfiguration.simulationsRootNode.path;
    const endpoint = this.storageConfiguration.storageApiUrl(`/set/file?path=${path}`);
    this.dialogs.open(FileUploadDialogComponent, DialogSize.SIZE_MD, {
      endpoint,
      multiple: false,
      accept: '.har',
      title: 'Upload HAR File',
    }).pipe(filter(filesNames => !!filesNames[0])).subscribe((fileNames: string[]) => {
      const harPath = path + '/' + fileNames[0];
      this.importHar({path: harPath} as StorageNode);
    });
  }

  importHar(node: StorageNode) {
    // this.dialogs.open(ImportHarDialogComponent, DialogSize.SIZE_MD)
    //   .subscribe((result: { simulationPackage: string, simulationClass: string }) => {
    //     this.analysis.record({
    //       GATLING_SIMULATION_PACKAGE: result.simulationPackage,
    //       GATLING_SIMULATION_CLASS: result.simulationClass,
    //       HAR_PATH: node.path.replace(this.gatlingConfiguration.simulationsRootNode.path + '/', ''),
    //     }).subscribe((commandId: string) => {
    //       this.commands.setCommandLabel(commandId, 'Import HAR file', node.path);
    //       this.eventBus.publish(new OpenResultsEvent());
    //     });
    //   });
  }
}
