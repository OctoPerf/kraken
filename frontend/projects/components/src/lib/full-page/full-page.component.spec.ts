import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {FullPageComponent} from './full-page.component';
import {Component, ViewChild} from '@angular/core';
import {By} from '@angular/platform-browser';
import {EventModule} from 'projects/event/src/lib/event.module';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabsAddedEvent} from 'projects/tabs/src/lib/tabs-added-event';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsPosition} from 'projects/tabs/src/lib/tabs-position';

@Component({
  selector: 'lib-test',
  template: `
    <lib-full-page>
      <div class="menu" full-page-menu>Menu</div>
      <div class="content" full-page-content>Content</div>
    </lib-full-page>
  `
})
class TestComponent {
  @ViewChild(FullPageComponent, {static: true}) fullPage: FullPageComponent;
}

describe('FullPageComponent', () => {
  let component: TestComponent;
  let fixture: ComponentFixture<TestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [EventModule],
      declarations: [TestComponent, FullPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should display menu and content', () => {
    expect(fixture.debugElement.query(By.css('[full-page-menu]')).nativeElement.innerHTML).toBe('Menu');
    expect(fixture.debugElement.query(By.css('[full-page-content]')).nativeElement.innerHTML).toBe('Content');
  });

  it('should leave place for tabs', fakeAsync(() => {
    const eventBus = TestBed.get(EventBusService);
    eventBus.publish(new TabsAddedEvent(TabsSide.BOTTOM, TabsPosition.START));
    eventBus.publish(new TabsAddedEvent(TabsSide.LEFT, TabsPosition.START));
    eventBus.publish(new TabsAddedEvent(TabsSide.TOP, TabsPosition.START));
    eventBus.publish(new TabsAddedEvent(TabsSide.RIGHT, TabsPosition.START));
    tick(100);
    fixture.detectChanges();
    expect(component.fullPage.tabsRight).toBe(true);
    expect(component.fullPage.tabsLeft).toBe(true);
    expect(component.fullPage.tabsBottom).toBe(true);
    expect(component.fullPage.tabsTop).toBe(true);
  }));
});
