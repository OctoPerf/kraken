import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DebugEditorContentService} from 'projects/analysis/src/lib/results/debug/debug-editor/debug-editor-content.service';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faExternalLinkAlt} from '@fortawesome/free-solid-svg-icons/faExternalLinkAlt';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faInfoCircle} from '@fortawesome/free-solid-svg-icons/faInfoCircle';
import {Portal} from '@angular/cdk/portal';
import {SplitPane} from 'projects/split/src/lib/split-pane';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {StringToolsService} from 'projects/tools/src/lib/string-tools.service';

library.add(faExternalLinkAlt, faInfoCircle);

@Component({
  selector: 'lib-debug-editor',
  templateUrl: './debug-editor.component.html',
  styleUrls: ['./debug-editor.component.scss'],
  providers: [
    {provide: StorageNodeEditorContentService, useClass: DebugEditorContentService}
  ]
})
export class DebugEditorComponent extends DefaultStorageNodeEditorComponent implements OnInit {

  readonly newTabIcon = new IconFa(faExternalLinkAlt, 'primary');
  readonly sessionIcon = new IconFa(faInfoCircle, 'accent');

  public debug: DebugEditorContentService;
  public splits: SplitPane[];
  @ViewChild('requestPortal', { static: true }) requestPortal: Portal<any>;
  @ViewChild('responsePortal', { static: true }) responsePortal: Portal<any>;

  constructor(@Inject(STORAGE_NODE) node: StorageNode,
              public contentService: StorageNodeEditorContentService,
              private dialogs: DialogService,
              private strings: StringToolsService) {
    super(node, contentService);
    this.debug = contentService as DebugEditorContentService;
  }

  ngOnInit() {
    super.ngOnInit();
    this.splits = [new SplitPane(this.requestPortal, 50, 20), new SplitPane(this.responsePortal, 50, 20)];
  }

  public inspectSession() {
    this.dialogs.inspect('Gatling Session', this.strings.replaceAll(this.debug.entry.session, ',', ',\n\t'));
  }
}
