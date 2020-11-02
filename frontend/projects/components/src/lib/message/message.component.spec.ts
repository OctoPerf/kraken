import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {MessageComponent} from 'projects/components/src/lib/message/message.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {ComponentsModule} from 'projects/components/src/lib/components.module';


describe('MessageComponent', () => {
  let component: MessageComponent;
  let fixture: ComponentFixture<MessageComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, ComponentsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
