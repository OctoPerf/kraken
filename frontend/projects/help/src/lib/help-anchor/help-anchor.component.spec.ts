import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {HelpAnchorComponent} from './help-anchor.component';

describe('HelpAnchorComponent', () => {
  let component: HelpAnchorComponent;
  let fixture: ComponentFixture<HelpAnchorComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ HelpAnchorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HelpAnchorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
