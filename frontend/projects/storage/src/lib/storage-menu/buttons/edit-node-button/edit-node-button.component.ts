import {Component, Input} from '@angular/core';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faEdit} from '@fortawesome/free-solid-svg-icons/faEdit';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faEdit);

@Component({
  selector: 'lib-edit-node-button',
  templateUrl: './edit-node-button.component.html',
})
export class EditNodeButtonComponent {

  readonly editIcon = new IconFa(faEdit);

  @Input() node: StorageNode;

  constructor(private storage: StorageService) {
  }

}
