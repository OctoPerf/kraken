import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HostsSelectorComponent} from './hosts-selector.component';

describe('HostsSelectorComponent', () => {
  let component: HostsSelectorComponent;
  let fixture: ComponentFixture<HostsSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HostsSelectorComponent],
    })
      .overrideTemplate(HostsSelectorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostsSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
