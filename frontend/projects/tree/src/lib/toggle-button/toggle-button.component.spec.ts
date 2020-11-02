import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ToggleButtonComponent} from './toggle-button.component';
import {TreeModule} from '../tree.module';
import {CdkTree, CdkTreeNode} from '@angular/cdk/tree';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';

describe('ToggleButtonComponent', () => {
  let component: ToggleButtonComponent;
  let fixture: ComponentFixture<ToggleButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule, TreeModule],
      providers: [
        {provide: CdkTree, useValue: jasmine.createSpyObj('CdkTree', [''])},
        {provide: CdkTreeNode, useValue: jasmine.createSpyObj('CdkTreeNode', [''])},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ToggleButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set expanded', () => {
    component.expanded = true;
    expect(component.state).toBe('expanded');
  });

  it('should set collapsed', () => {
    component.expanded = false;
    expect(component.state).toBe('collapsed');
  });
});
