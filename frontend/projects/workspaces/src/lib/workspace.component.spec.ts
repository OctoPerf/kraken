import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkspaceComponent} from './workspace.component';
import {Component, NgModule} from '@angular/core';
import {WorkspacesModule} from './workspaces.module';
import {ComponentPortal} from '@angular/cdk/portal';
import {EMPTY_SIDE_CONFIG, SideConfiguration} from './side-configuration';
import {TabsConfiguration} from './tabs-configuration';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

@NgModule({
  imports: [CoreTestModule, WorkspacesModule],
  declarations: [TestComponent],
  entryComponents: [
    TestComponent,
  ],
})
class TestModule {
}

describe('WorkspaceComponent', () => {
  let component: WorkspaceComponent;
  let fixture: ComponentFixture<WorkspaceComponent>;
  const tabsConf = new TabsConfiguration(
    [newTestTab(TestComponent)],
    0,
    50
  );
  const sideConf = new SideConfiguration(
    tabsConf,
    tabsConf,
    20,
  );

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkspaceComponent);
    component = fixture.componentInstance;
  });

  it('should create with all sides', () => {
    component.center = new ComponentPortal(TestComponent);
    component.centerMinWidth = 40;
    component.centerMinHeight = 40;
    component.left = sideConf;
    component.right = sideConf;
    component.bottom = sideConf;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.verticalSplits.length).toBe(2);
    expect(component.horizontalSplits.length).toBe(3);
  });

  it('should create no bottom', () => {
    component.center = new ComponentPortal(TestComponent);
    component.left = sideConf;
    component.right = sideConf;
    component.bottom = EMPTY_SIDE_CONFIG;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.verticalSplits.length).toBe(1);
    expect(component.horizontalSplits.length).toBe(3);
  });

  it('should create no right', () => {
    component.center = new ComponentPortal(TestComponent);
    component.left = sideConf;
    component.right = EMPTY_SIDE_CONFIG;
    component.bottom = sideConf;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.verticalSplits.length).toBe(2);
    expect(component.horizontalSplits.length).toBe(2);
  });

  it('should create no right', () => {
    component.center = new ComponentPortal(TestComponent);
    component.left = EMPTY_SIDE_CONFIG;
    component.right = sideConf;
    component.bottom = sideConf;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.verticalSplits.length).toBe(2);
    expect(component.horizontalSplits.length).toBe(2);
  });

  it('should create only bottom', () => {
    component.center = new ComponentPortal(TestComponent);
    component.left = EMPTY_SIDE_CONFIG;
    component.right = EMPTY_SIDE_CONFIG;
    component.bottom = sideConf;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.verticalSplits.length).toBe(2);
    expect(component.horizontalSplits.length).toBe(1);
  });


  describe('with all tabs', () => {
    beforeEach(() => {
      component.center = new ComponentPortal(TestComponent);
      component.left = sideConf;
      component.right = sideConf;
      component.bottom = sideConf;
      fixture.detectChanges();
    });

    it('should bottomPaneExpanded', () => {
      const spy = spyOn(component.verticalSplit, 'show');
      component.bottomPaneExpanded();
      expect(spy).toHaveBeenCalledWith(1);
    });

    it('should bottomPaneCollapsed', () => {
      const spy = spyOn(component.verticalSplit, 'hide');
      component.bottomPaneCollapsed();
      expect(spy).toHaveBeenCalledWith(1);
    });

    it('should bottomPaneHidden', () => {
      const spy = spyOn(component.bottomSideSplit, 'closeTabs');
      component.bottomPaneHidden();
      expect(spy).toHaveBeenCalled();
    });

    it('should bottomPaneHidden', () => {
      const spy = spyOn(component.bottomSideSplit, 'closeTabs');
      component.bottomPaneHidden();
      expect(spy).toHaveBeenCalled();
    });

    it('should leftPaneExpanded', () => {
      const spy = spyOn(component.horizontalSplit, 'show');
      component.leftPaneExpanded();
      expect(spy).toHaveBeenCalledWith(0);
    });

    it('should leftPaneCollapsed', () => {
      const spy = spyOn(component.horizontalSplit, 'hide');
      component.leftPaneCollapsed();
      expect(spy).toHaveBeenCalledWith(0);
    });

    it('should rightPaneExpanded', () => {
      const spy = spyOn(component.horizontalSplit, 'show');
      component.rightPaneExpanded();
      expect(spy).toHaveBeenCalledWith(2);
    });

    it('should rightPaneCollapsed', () => {
      const spy = spyOn(component.horizontalSplit, 'hide');
      component.rightPaneCollapsed();
      expect(spy).toHaveBeenCalledWith(2);
    });

    it('should leftRightPaneHidden left', () => {
      const spy = spyOn(component.leftSideSplit, 'closeTabs');
      component.leftRightPaneHidden([0, null]);
      expect(spy).toHaveBeenCalled();
    });

    it('should leftRightPaneHidden right', () => {
      const spy = spyOn(component.rightSideSplit, 'closeTabs');
      component.leftRightPaneHidden([2, null]);
      expect(spy).toHaveBeenCalled();
    });

    it('should leftRightPaneHidden kamoulox', () => {
      component.leftRightPaneHidden([42, null]);
      expect().nothing();
    });

  });
});
