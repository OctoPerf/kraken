import { ColorModule } from './color.module';

describe('ColorModule', () => {
  let colorModule: ColorModule;

  beforeEach(() => {
    colorModule = new ColorModule();
  });

  it('should create an instance', () => {
    expect(colorModule).toBeTruthy();
  });
});
