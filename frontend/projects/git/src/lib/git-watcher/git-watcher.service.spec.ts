import {TestBed} from '@angular/core/testing';

import {GitWatcherService} from './git-watcher.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {testGitLog} from 'projects/git/src/lib/entities/git-log.spec';
import {GitLogEvent} from 'projects/git/src/lib/events/git-log-event';
import {testGitStatus} from 'projects/git/src/lib/entities/git-status.spec';
import {GitStatusEvent} from 'projects/git/src/lib/events/git-status-event';
import {testUserOwner} from 'projects/security/src/lib/entities/owner.spec';
import {GitRefreshStorageEvent} from 'projects/git/src/lib/events/git-refresh-storage-event';

describe('GitWatcherService', () => {
  let service: GitWatcherService;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        EventBusService,
        GitWatcherService,
      ]
    });
    eventBus = TestBed.inject(EventBusService);
    service = TestBed.inject(GitWatcherService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle git log event', () => {
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'GIT_LOG', value: testGitLog()}));
    expect(spy).toHaveBeenCalledWith(new GitLogEvent(testGitLog().text));
  });

  it('should handle git status event', () => {
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'GIT_STATUS', value: testGitStatus()}));
    expect(spy).toHaveBeenCalledWith(new GitStatusEvent(testGitStatus()));
  });

  it('should handle git refresh event', () => {
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'GIT_REFRESH', value: {owner: testUserOwner()}}));
    expect(spy).toHaveBeenCalledWith(new GitRefreshStorageEvent());
  });
});
