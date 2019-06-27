import {Component, InjectionToken, Injector, OnInit} from '@angular/core';
import {SideConfiguration} from 'projects/workspaces/src/lib/side-configuration';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {EMPTY_TABS_CONFIG, TabsConfiguration} from 'projects/workspaces/src/lib/tabs-configuration';
import {Tab} from 'projects/tabs/src/lib/tab';
import {HelpPanelComponent} from 'projects/help/src/lib/help-panel/help-panel.component';
import {faQuestionCircle} from '@fortawesome/free-regular-svg-icons/faQuestionCircle';
import {NotificationsTableComponent} from 'projects/notification/src/lib/notifications-table/notifications-table.component';
import {NotificationsTabHeaderComponent} from 'projects/notification/src/lib/notifications-tab-header/notifications-tab-header.component';
import {library} from '@fortawesome/fontawesome-svg-core';
import {transition, trigger, useAnimation} from '@angular/animations';
import {fadeInAnimation} from 'projects/commons/src/lib/animations';
import {ImagesTableComponent} from 'projects/docker/src/lib/docker-client/images-table/images-table.component';
import {faArchive} from '@fortawesome/free-solid-svg-icons/faArchive';
import {ContainersTableComponent} from 'projects/docker/src/lib/docker-client/containers-table/containers-table.component';
import {faDocker} from '@fortawesome/free-brands-svg-icons/faDocker';
import {
  STORAGE_CONTEXTUAL_MENU,
  StorageTreeComponent
} from 'projects/storage/src/lib/storage-tree/storage-tree/storage-tree.component';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {faFolder} from '@fortawesome/free-regular-svg-icons/faFolder';
import {faFolderOpen} from '@fortawesome/free-regular-svg-icons/faFolderOpen';
import {StorageEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-editor/storage-editor.component';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';
import {OpenNotificationsEvent} from 'projects/notification/src/lib/open-notifications-event';
import {OpenCommandLogsEvent} from 'projects/command/src/lib/entities/open-command-logs-event';
import {CommandTabsPanelComponent} from 'projects/command/src/lib/command-tabs-panel/command-tabs-panel.component';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {STORAGE_ROOT_NODE} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {AdministrationContextualMenuComponent} from 'projects/administration/src/app/contextual-menu/administration-contextual-menu/administration-contextual-menu.component';
import {OpenStorageTreeEvent} from 'projects/storage/src/lib/events/open-storage-tree-event';

library.add(faFolder, faFolderOpen, faQuestionCircle, faBell, faDocker, faArchive);

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

  constructor(private injector: Injector) {
  }

  ngOnInit() {
    this.center = new ComponentPortal<StorageEditorComponent>(StorageEditorComponent);


    const treeRoot: StorageNode = {
      path: '',
      type: 'DIRECTORY',
      depth: -1,
      length: 0,
      lastModified: 0,
    };

    const adminTree = new ComponentPortal(StorageTreeComponent,
      null,
      new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
        [STORAGE_ROOT_NODE, treeRoot],
        [STORAGE_CONTEXTUAL_MENU, AdministrationContextualMenuComponent]
      ])));

    this.left = new SideConfiguration(
      new TabsConfiguration(
        [new Tab(adminTree, 'Configuration',
          new IconDynamic(new IconFa(faFolder), {'selected': new IconFa(faFolderOpen)}),
          'ADMIN_CONFIGURATION',
          false,
          [OpenStorageTreeEvent.CHANNEL])],
        0,
        100,
      ),
      EMPTY_TABS_CONFIG,
      25,
    );

    this.right = new SideConfiguration(
      new TabsConfiguration(
        [
          new Tab(new ComponentPortal(HelpPanelComponent), 'Help', new IconFa(faQuestionCircle, 'accent'), null, false,
            [OpenHelpEvent.CHANNEL]),
        ],
        -1,
        100
      ),
      EMPTY_TABS_CONFIG,
      25
    );

    this.bottom = new SideConfiguration(
      new TabsConfiguration(
        [
          new Tab(
            new ComponentPortal(ContainersTableComponent),
            'Docker Containers',
            new IconFa(faDocker),
            'ADMIN_CONTAINERS',
            false,
            ['open-docker-client-containers']),
          new Tab(
            new ComponentPortal(ImagesTableComponent),
            'Docker Images',
            new IconFa(faArchive),
            'ADMIN_IMAGES',
            false,
            ['open-docker-client-images']),
          new Tab(
            new ComponentPortal(CommandTabsPanelComponent),
            'Command Executions',
            PLAY_ICON,
            'ADMIN_EXECUTIONS',
            true,
            [OpenCommandLogsEvent.CHANNEL]),
        ],
        0,
        50
      ),
      new TabsConfiguration(
        [
          new Tab(new ComponentPortal(NotificationsTableComponent),
            'Notifications',
            new IconFa(faBell),
            null,
            false,
            [OpenNotificationsEvent.CHANNEL],
            NotificationsTabHeaderComponent),
        ],
        -1,
        50
      ),
      40
    );
  }
}
