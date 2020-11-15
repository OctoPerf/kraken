import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ContainerStatusComponent} from './container-status.component';

describe('ContainerStatusComponent', () => {
  let component: ContainerStatusComponent;
  let fixture: ComponentFixture<ContainerStatusComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ContainerStatusComponent]
    })
      .overrideTemplate(ContainerStatusComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set status', () => {
    component.status = 'RUNNING';
    expect(component.status).toBe('RUNNING');
    expect(component.value).toBe(60);
  });
});
