import {Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {GitStatus} from 'projects/git/src/lib/entities/git-status';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {GitStatusEvent} from 'projects/git/src/lib/events/git-status-event';
import * as _ from 'lodash';
import {GitFileStatusEvent} from 'projects/git/src/lib/events/git-file-status-event';
import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';
import {GitRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status';
import {XyToColorPipe} from 'projects/git/src/lib/git-command/xy-to-color.pipe';

@Injectable({
  providedIn: 'root'
})
export class GitFileStatusService implements OnDestroy {

  private subscriptions: Subscription[] = [];
  private previous: GitFileStatusEvent[] = [];

  constructor(private eventBus: EventBusService) {
    this.subscriptions.push(
      this.eventBus.of<GitStatusEvent>(GitStatusEvent.CHANNEL)
        .subscribe(event => this._handleStatus(event.status))
    );
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  public getEvent(path: string) {
    const event = _.find(this.previous, {path});
    return event || new GitFileStatusEvent(path, '..');
  }

  _handleStatus(status: GitStatus): void {
    this.previous.forEach(event => this.eventBus.publish(new GitFileStatusEvent(event.path, '..', 'foreground')));
    const events = this._statusToEvents(status);
    this.previous = events;
    events.forEach(event => this.eventBus.publish(event));
  }

  _statusToEvents(status: GitStatus): GitFileStatusEvent[] {
    return [].concat(status.untracked.map(path => new GitFileStatusEvent(path, '??', 'error')),
      status.ignored.map(path => new GitFileStatusEvent(path, '!!')),
      status.changed.map(fileStatus => this._fileStatusToEvent(fileStatus)),
      status.renamedCopied.map(fileStatus => this._fileStatusToEvent(fileStatus)),
      status.unmerged.map(fileStatus => this._fileStatusToEvent(fileStatus)));
  }

  _fileStatusToEvent(fileStatus: GitFileStatus | GitRenamedCopiedStatus): GitFileStatusEvent {
    return new GitFileStatusEvent(fileStatus.path, fileStatus.xy, XyToColorPipe.xyToColor(fileStatus.xy.charAt(0)));
  }
}
