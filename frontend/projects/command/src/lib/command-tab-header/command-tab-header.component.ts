import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {CLOSE_ICON, LOADING_ICON} from 'projects/icon/src/lib/icons';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faStop} from '@fortawesome/free-solid-svg-icons/faStop';
import {library} from '@fortawesome/fontawesome-svg-core';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {CommandService} from 'projects/command/src/lib/command.service';
import {Subscription} from 'rxjs';

library.add(faStop);

@Component({
  selector: 'lib-command-tab-header',
  templateUrl: './command-tab-header.component.html',
  styleUrls: ['./command-tab-header.component.scss']
})
export class CommandTabHeaderComponent implements OnInit, OnDestroy {

  readonly loadingIcon = LOADING_ICON;
  readonly closeIcon = CLOSE_ICON;
  readonly stopIcon = new IconDynamic(new IconFa(faStop), {'selected': new IconFa(faStop, 'error')});

  @Input() log: CommandLog;
  @Input() selected: boolean;
  @Output() closeCommand = new EventEmitter<void>();
  @Output() stopCommand = new EventEmitter<void>();
  @Output() openContextualMenu = new EventEmitter<MouseEvent>();

  name = '';
  title = '';

  _subscription: Subscription;

  constructor(private commands: CommandService) {
  }

  ngOnInit() {
    this._updateHeader();
    this._subscription = this.commands.commandLabelsChanged.subscribe(this._updateHeader.bind(this));
  }

  ngOnDestroy() {
    this.commands.removeCommandLabel(this.log.command.id);
    this._subscription.unsubscribe();
  }

  _updateHeader() {
    this.name = this.commands.getCommandName(this.log.command);
    this.title = this.commands.getCommandTitle(this.log.command);
  }
}
