import {Component} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';

@Component({
  selector: 'lib-editor-messages',
  templateUrl: './editor-messages.component.html',
  styleUrls: ['./editor-messages.component.scss']
})
export class EditorMessagesComponent {

  constructor(public contentService: StorageNodeEditorContentService) {
  }

}
