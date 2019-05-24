import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SpinnerComponent} from './spinner.component';
import {ComponentsModule} from '../components.module';

describe('SpinnerComponent', () => {
  let component: SpinnerComponent;
  let fixture: ComponentFixture<SpinnerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ ComponentsModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpinnerComponent);
    component = fixture.componentInstance;
    component.color = 'success';
    component.size = 42;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
