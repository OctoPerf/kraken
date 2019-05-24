import { DateModule } from './date.module';

describe('DateModule', () => {
  let dateModule: DateModule;

  beforeEach(() => {
    dateModule = new DateModule();
  });

  it('should create an instance', () => {
    expect(dateModule).toBeTruthy();
  });
});
