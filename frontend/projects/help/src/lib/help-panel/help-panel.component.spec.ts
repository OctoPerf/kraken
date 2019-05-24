import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';
import {HelpPanelComponent} from 'projects/help/src/lib/help-panel/help-panel.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SplitPanesDragStart} from 'projects/split/src/lib/split-panes/split-panes-drag-start';
import {SplitPanesDragStop} from 'projects/split/src/lib/split-panes/split-panes-drag-stop';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

describe('HelpPanelComponent', () => {
  let component: HelpPanelComponent;
  let fixture: ComponentFixture<HelpPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, HelpModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HelpPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(fixture.nativeElement.querySelector('iframe').getAttribute('src')).toBe('docUrl/');
  });

  it('should deactivate mouse event while a split pane is dragged', inject([EventBusService], (eventBus: EventBusService) => {
    // https://stackoverflow.com/questions/5645485/detect-mousemove-when-over-an-iframe
    expect(component.pointerEvents).toBe('auto');
    eventBus.publish(new SplitPanesDragStart());
    expect(component.pointerEvents).toBe('none');
    eventBus.publish(new SplitPanesDragStop());
    expect(component.pointerEvents).toBe('auto');
  }));

  it('should open help pageId', inject([EventBusService], (eventBus: EventBusService) => {
    eventBus.publish(new OpenHelpEvent('TEST'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('iframe').getAttribute('src')).toBe('docUrl/test');
  }));

});
