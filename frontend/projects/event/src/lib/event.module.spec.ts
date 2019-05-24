import { EventModule } from './event.module';

describe('EventModule', () => {
  let eventsModule: EventModule;

  beforeEach(() => {
    eventsModule = new EventModule();
  });

  it('should create an instance', () => {
    expect(eventsModule).toBeTruthy();
  });
});
