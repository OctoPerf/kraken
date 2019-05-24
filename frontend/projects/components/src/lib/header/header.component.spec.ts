import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {ComponentsModule} from 'projects/components/src/lib/components.module';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, ComponentsModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
