import {Component, Inject} from '@angular/core';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {DockerComposeService} from 'projects/docker/src/lib/docker-compose/docker-compose.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faStop} from '@fortawesome/free-solid-svg-icons/faStop';
import {faFileAlt} from '@fortawesome/free-solid-svg-icons/faFileAlt';
import {faList} from '@fortawesome/free-solid-svg-icons/faList';
import {library} from '@fortawesome/fontawesome-svg-core';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';

library.add(faStop, faFileAlt, faList);

@Component({
  selector: 'lib-docker-compose-editor',
  templateUrl: './docker-compose-editor.component.html',
  styleUrls: ['./docker-compose-editor.component.scss'],
  providers: [
    StorageNodeEditorContentService,
  ]
})
export class DockerComposeEditorComponent extends DefaultStorageNodeEditorComponent {

  public readonly upIcon = PLAY_ICON;
  public readonly downIcon = new IconFa(faStop, 'error');
  public readonly logsIcon = new IconFa(faFileAlt);
  public readonly psIcon = new IconFa(faList);

  constructor(@Inject(STORAGE_NODE) node: StorageNode,
              contentService: StorageNodeEditorContentService,
              private dockerComposeService: DockerComposeService,
              private toParentPath: StorageNodeToParentPathPipe) {
    super(node, contentService);
  }

  command(command: 'up' | 'down' | 'ps' | 'logs') {
    this.dockerComposeService.command(command, this.toParentPath.transform(this.node)).subscribe();
  }

}
