import { VendorsModule } from './vendors.module';

describe('VendorsModule', () => {
  let vendorsModule: VendorsModule;

  beforeEach(() => {
    vendorsModule = new VendorsModule();
  });

  it('should create an instance', () => {
    expect(vendorsModule).toBeTruthy();
  });
});
