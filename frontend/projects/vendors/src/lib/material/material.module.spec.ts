import {MaterialModule} from './material.module';

export const matSnackBarSpy = () => jasmine.createSpyObj('matSnackBarSpy', ['open']);

describe('MaterialModule', () => {
  let materialModule: MaterialModule;

  beforeEach(() => {
    materialModule = new MaterialModule();
  });

  it('should create an instance', () => {
    expect(materialModule).toBeTruthy();
  });
});
