import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {HostCapacityComponent} from './host-capacity.component';

describe('HostCapacityComponent', () => {
  let component: HostCapacityComponent;
  let fixture: ComponentFixture<HostCapacityComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [HostCapacityComponent]
    })
      .overrideTemplate(HostCapacityComponent, '')
      .compileComponents();
  }));

  it('should create', () => {
    fixture = TestBed.createComponent(HostCapacityComponent);
    component = fixture.componentInstance;
    component.allocatable = '12.6Mb';
    component.capacity = '20Mb';
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.progress).toBe(66.7);
    expect(component.color).toBe('accent');
  });

  it('should create empty', () => {
    fixture = TestBed.createComponent(HostCapacityComponent);
    component = fixture.componentInstance;
    component.allocatable = '-';
    component.capacity = '-';
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.progress).toBe(0);
    expect(component.color).toBeUndefined();
  });
});
