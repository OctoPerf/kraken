import {Component, InjectionToken, Injector, OnInit} from '@angular/core';
import {SideConfiguration} from 'projects/workspaces/src/lib/side-configuration';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {TabsConfiguration} from 'projects/workspaces/src/lib/tabs-configuration';
import {Tab} from 'projects/tabs/src/lib/tab';
import {HelpPanelComponent} from 'projects/help/src/lib/help-panel/help-panel.component';
import {faQuestionCircle} from '@fortawesome/free-regular-svg-icons/faQuestionCircle';
import {NotificationsTableComponent} from 'projects/notification/src/lib/notifications-table/notifications-table.component';
import {NotificationsTabHeaderComponent} from 'projects/notification/src/lib/notifications-tab-header/notifications-tab-header.component';
import {library} from '@fortawesome/fontawesome-svg-core';
import {transition, trigger, useAnimation} from '@angular/animations';
import {fadeInAnimation} from 'projects/commons/src/lib/animations';
import {
  STORAGE_CONTEXTUAL_MENU,
  STORAGE_TREE_LABEL,
  StorageTreeComponent
} from 'projects/storage/src/lib/storage-tree/storage-tree/storage-tree.component';
import {
  STORAGE_EDITOR_README_NODE,
  StorageEditorComponent
} from 'projects/storage/src/lib/storage-editor/storage-editor/storage-editor.component';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';
import {OpenNotificationsEvent} from 'projects/notification/src/lib/open-notifications-event';
import {DEBUG_ICON, LOGS_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {STORAGE_ROOT_NODE} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {STORAGE_NODE_BUTTONS} from 'projects/storage/src/lib/storage-tree/storage-node/storage-node.component';
import {SimulationNodeButtonsComponent} from 'projects/gatling/src/app/simulations/simulation-node-buttons/simulation-node-buttons.component';
import {faCode} from '@fortawesome/free-solid-svg-icons/faCode';
import {faFile} from '@fortawesome/free-solid-svg-icons/faFile';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {SimulationContextualMenuComponent} from 'projects/gatling/src/app/simulations/simulation-contextual-menu/simulation-contextual-menu.component';
import {ResultsTableComponent} from 'projects/analysis/src/lib/results/results-table/results-table.component';
import {faPoll} from '@fortawesome/free-solid-svg-icons/faPoll';
import {OpenStorageTreeEvent} from 'projects/storage/src/lib/events/open-storage-tree-event';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {DebugEntriesTableComponent} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.component';
import {OpenDebugEvent} from 'projects/analysis/src/lib/events/open-debug-event';
import {OpenResultsEvent} from 'projects/analysis/src/lib/events/open-results-event';
import {TasksTableComponent} from 'projects/runtime/src/lib/runtime-task/tasks-table/tasks-table.component';
import {RuntimeLogsPanelComponent} from 'projects/runtime/src/lib/runtime-log/runtime-logs-panel/runtime-logs-panel.component';
import {OpenLogsEvent} from 'projects/runtime/src/lib/events/open-logs-event';
import {faDocker} from '@fortawesome/free-brands-svg-icons/faDocker';
import {ContainersTableComponent} from 'projects/runtime/src/lib/runtime-task/containers-table/containers-table.component';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import {OpenTasksEvent} from 'projects/runtime/src/lib/events/open-tasks-event';

library.add(faCode, faQuestionCircle, faBell, faFile, faPoll, faDocker);

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.scss'],
  animations: [
    trigger('insertWorkspace', [
      transition(':enter', useAnimation(fadeInAnimation, {params: {duration: '1s'}}))
    ]),
  ],
})
export class WorkspaceComponent implements OnInit {

  left: SideConfiguration;
  right: SideConfiguration;
  bottom: SideConfiguration;
  center: ComponentPortal<StorageEditorComponent>;

  constructor(private injector: Injector,
              private gatlingConfiguration: GatlingConfigurationService) {
  }

  ngOnInit() {
    this.center = new ComponentPortal(StorageEditorComponent,
      null,
      new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
        [STORAGE_EDITOR_README_NODE, {path: 'gatling/README.md', type: 'FILE', depth: 1}],
      ])));

    const simulationsTree = new ComponentPortal(StorageTreeComponent,
      null,
      new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
        [STORAGE_ROOT_NODE, this.gatlingConfiguration.simulationsRootNode],
        [STORAGE_ID, 'simulations-tree'],
        [STORAGE_TREE_LABEL, 'Simulations'],
        [STORAGE_NODE_BUTTONS, SimulationNodeButtonsComponent],
        [STORAGE_CONTEXTUAL_MENU, SimulationContextualMenuComponent]
      ])));

    const resourcesTree = new ComponentPortal(StorageTreeComponent,
      null,
      new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
        [STORAGE_ROOT_NODE, this.gatlingConfiguration.resourcesRootNode],
        [STORAGE_TREE_LABEL, 'Resource Files'],
        [STORAGE_ID, 'resources-tree'],
      ])));

    this.left = new SideConfiguration(
      new TabsConfiguration(
        [new Tab(simulationsTree, 'Simulations',
          new IconFa(faCode),
          'GATLING_SIMULATIONS',
          false,
          [OpenStorageTreeEvent.CHANNEL])],
        0,
        60,
      ),
      new TabsConfiguration(
        [new Tab(resourcesTree, 'Resources',
          new IconFa(faFile),
          'GATLING_RESOURCES',
          false)],
        0,
        40,
      ),
      25,
    );

    this.right = new SideConfiguration(
      new TabsConfiguration(
        [
          new Tab(new ComponentPortal(HelpPanelComponent), 'Help', new IconFa(faQuestionCircle, 'accent'),
            null,
            false,
            [OpenHelpEvent.CHANNEL]),
          new Tab(new ComponentPortal(ResultsTableComponent), 'Results',
            new IconFa(faPoll),
            'GATLING_RESULTS_TABLE',
            false,
            [OpenResultsEvent.CHANNEL]),
        ],
        -1,
        50
      ),

      // new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
      //         [STORAGE_ROOT_NODE, this.gatlingConfiguration.simulationsRootNode],
      //         [STORAGE_ID, 'simulations-tree'],
      //         [STORAGE_TREE_LABEL, 'Simulations'],
      //         [STORAGE_NODE_BUTTONS, SimulationNodeButtonsComponent],
      //         [STORAGE_CONTEXTUAL_MENU, SimulationContextualMenuComponent]
      //       ])));
      new TabsConfiguration(
        [
          new Tab(new ComponentPortal(DebugEntriesTableComponent), 'Debug', DEBUG_ICON,
            'GATLING_DEBUG_ENTRIES_TABLE',
            false,
            [OpenDebugEvent.CHANNEL]),
        ],
        -1,
        50
      ),
      30
    );

    this.bottom = new SideConfiguration(
      new TabsConfiguration(
        [
          new Tab(
            new ComponentPortal(TasksTableComponent),
            'Tasks',
            PLAY_ICON,
            'GATLING_TASKS_TABLE',
            true,
            [OpenTasksEvent.CHANNEL]),
          new Tab(
            new ComponentPortal(RuntimeLogsPanelComponent),
            'Logs',
            LOGS_ICON,
            'GATLING_LOGS',
            true,
            [OpenLogsEvent.CHANNEL]),
        ],
        0,
        60
      ),
      new TabsConfiguration(
        [
          new Tab(
            new ComponentPortal(ContainersTableComponent),
            'Containers',
            new IconFa(faDocker),
            'GATLING_CONTAINERS_TABLE',
            true,
            [TaskSelectedEvent.CHANNEL]),
          new Tab(new ComponentPortal(NotificationsTableComponent),
            'Notifications',
            new IconFa(faBell),
            null,
            false,
            [OpenNotificationsEvent.CHANNEL],
            NotificationsTabHeaderComponent),
        ],
        0,
        40
      ),
      30
    );
  }
}
