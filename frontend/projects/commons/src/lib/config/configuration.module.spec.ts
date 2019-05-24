import {ConfigurationModule} from './configuration.module';

describe('ConfigModule', () => {
  let configModule: ConfigurationModule;

  beforeEach(() => {
    configModule = new ConfigurationModule();
  });

  it('should create an instance', () => {
    expect(configModule).toBeTruthy();
  });

  it('should create a forRoot instance', () => {
    expect(ConfigurationModule.forRoot({})).toBeTruthy();
  });
});
