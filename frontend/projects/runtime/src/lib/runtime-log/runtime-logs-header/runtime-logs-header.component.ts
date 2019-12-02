import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {CLOSE_ICON, LOADING_ICON} from 'projects/icon/src/lib/icons';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faStop} from '@fortawesome/free-solid-svg-icons/faStop';
import {Subscription} from 'rxjs';
import {RuntimeLogService} from 'projects/runtime/src/lib/runtime-log/runtime-log.service';
import {Log} from 'projects/runtime/src/lib/entities/log';

@Component({
  selector: 'lib-runtime-logs-header',
  templateUrl: './runtime-logs-header.component.html',
  styleUrls: ['./runtime-logs-header.component.scss']
})
export class RuntimeLogsHeaderComponent implements OnInit, OnDestroy {

  readonly loadingIcon = LOADING_ICON;
  readonly closeIcon = CLOSE_ICON;
  readonly stopIcon = new IconDynamic(new IconFa(faStop), {'selected': new IconFa(faStop, 'error')});

  @Input() log: Log;
  @Input() selected: boolean;
  @Output() close = new EventEmitter<void>();
  @Output() stop = new EventEmitter<void>();
  @Output() openContextualMenu = new EventEmitter<MouseEvent>();

  name = '';
  title = '';

  _subscription: Subscription;

  constructor(private logService: RuntimeLogService) {
  }

  ngOnInit() {
    this._updateHeader();
    this._subscription = this.logService.logLabelsChanged.subscribe(this._updateHeader.bind(this));
  }

  ngOnDestroy() {
    this.logService.removeLabel(this.log.id);
    this._subscription.unsubscribe();
  }

  _updateHeader() {
    const label = this.logService.label(this.log.id);
    this.name = label.name;
    this.title = label.title;
  }
}
