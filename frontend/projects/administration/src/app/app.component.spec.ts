import {TestBed, waitForAsync} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

describe('AppComponent', () => {
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [
        AppComponent
      ],
      providers: [
        {provide: ConfigurationService, useValue: configurationServiceMock()}
      ]
    }).overrideTemplate(AppComponent, '')
      .compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

});
