import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {InlineHelpComponent} from 'projects/help/src/lib/inline-help/inline-help.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {HelpModule} from 'projects/help/src/lib/help.module';

describe('InlineHelpComponent', () => {
  let component: InlineHelpComponent;
  let fixture: ComponentFixture<InlineHelpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, HelpModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InlineHelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
