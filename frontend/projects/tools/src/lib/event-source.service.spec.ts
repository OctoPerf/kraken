import {TestBed} from '@angular/core/testing';

import {EventSourceService} from './event-source.service';
import {ToolsModule} from './tools.module';

export const eventSourceSpy = () => {
  const spy = jasmine.createSpyObj('EventSource', ['onerror', 'onmessage', 'onopen', 'close']);
  spy.readyState = 0;
  spy.CONNECTING = 0;
  spy.OPEN = 1;
  spy.CLOSED = 2;
  return spy;
};

export const eventSourceServiceSpy = () => jasmine.createSpyObj('EventSourceService', ['newEventSource', 'newObservable']);

describe('EventSourceService', () => {
  let service: EventSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ToolsModule]
    });
    service = TestBed.get(EventSourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return eventSource', () => {
    expect(service.newEventSource('test')).toBeTruthy();
  });

  it('should newObservable cancel', () => {
    const eventSource = eventSourceSpy();
    spyOn(service, 'newEventSource').and.returnValue(eventSource);
    const observable = service.newObservable('path');
    observable.subscribe().unsubscribe();
    expect(service.newEventSource).toHaveBeenCalledWith('path');
    expect(eventSource.close).toHaveBeenCalled();
  });

  it('should newObservable succeed', () => {
    const eventSource = eventSourceSpy();
    spyOn(service, 'newEventSource').and.returnValue(eventSource);
    const observable = service.newObservable<string>('path');
    let complete = false;
    let data: string;
    observable.subscribe(d => data = d, () => {
      console.log('FUUUUU');
    }, () => complete = true);
    expect(service.newEventSource).toHaveBeenCalledWith('path');
    eventSource.onmessage({data: 'data'});
    eventSource.onerror(null);
    expect(data).toBe('data');
    expect(eventSource.close).toHaveBeenCalled();
    expect(complete).toBeTruthy();
  });

  it('should newObservable fail', () => {
    const eventSource = eventSourceSpy();
    spyOn(service, 'newEventSource').and.returnValue(eventSource);
    const observable = service.newObservable<string>('path', {errorMessage: 'error'});
    let error;
    observable.subscribe(() => {
    }, (err) => error = err, () => {
    });
    expect(service.newEventSource).toHaveBeenCalledWith('path');
    (eventSource as any).readyState = 2;
    eventSource.onerror(null);
    expect(eventSource.close).toHaveBeenCalled();
    expect(error).toBeDefined();
  });
});
