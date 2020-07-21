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
import {StorageTreeComponent} from 'projects/storage/src/lib/storage-tree/storage-tree/storage-tree.component';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {faFolder} from '@fortawesome/free-regular-svg-icons/faFolder';
import {faFolderOpen} from '@fortawesome/free-regular-svg-icons/faFolderOpen';
import {StorageEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-editor/storage-editor.component';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';
import {OpenNotificationsEvent} from 'projects/notification/src/lib/open-notifications-event';
import {ROOT_NODE, StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {STORAGE_ROOT_NODE} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {OpenStorageTreeEvent} from 'projects/storage/src/lib/events/open-storage-tree-event';
import {HostsTableComponent} from 'projects/runtime/src/lib/runtime-host/hosts-table/hosts-table.component';
import {faServer} from '@fortawesome/free-solid-svg-icons/faServer';

library.add(faFolder, faFolderOpen, faQuestionCircle, faBell, faServer);

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

    const adminTree = new ComponentPortal(StorageTreeComponent,
      null,
      new PortalInjector(this.injector, new WeakMap<InjectionToken<any>, any>([
        [STORAGE_ROOT_NODE, ROOT_NODE],
      ])));

    this.left = new SideConfiguration(
      new TabsConfiguration(
        [new Tab(adminTree, 'Applications',
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
          new Tab(new ComponentPortal(HostsTableComponent), 'Hosts', new IconFa(faServer), 'ADMIN_HOSTS_TABLE', true, []),
        ],
        0,
        70
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
        30
      ),
      40
    );
  }
}
