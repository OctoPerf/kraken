import {TestBed} from '@angular/core/testing';

import {GitFileStatusService} from './git-file-status.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {GitFileStatusEvent} from 'projects/git/src/lib/events/git-file-status-event';
import {testGitStatus} from 'projects/git/src/lib/entities/git-status.spec';
import {GitStatusEvent} from 'projects/git/src/lib/events/git-status-event';

describe('GitFileStatusService', () => {
  let service: GitFileStatusService;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GitFileStatusService);
    eventBus = TestBed.inject(EventBusService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle status event', () => {
    const events = [];
    eventBus.of<GitFileStatusEvent>(GitFileStatusEvent.CHANNEL).subscribe(value => events.push(value));
    eventBus.publish(new GitStatusEvent(testGitStatus()));
    expect(events.length).toBe(5);

    expect(service.getEvent('path')).toBeDefined();

    eventBus.publish(new GitStatusEvent(testGitStatus()));
    expect(events.length).toBe(15);
  });
});
