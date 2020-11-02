import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {SimulationNodeButtonsComponent} from './simulation-node-buttons.component';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('SimulationNodeButtonsComponent', () => {
  let component: SimulationNodeButtonsComponent;
  let fixture: ComponentFixture<SimulationNodeButtonsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SimulationNodeButtonsComponent],
      providers: [
        {provide: STORAGE_NODE, useValue: testStorageFileNode()}
      ]
    })
      .overrideTemplate(SimulationNodeButtonsComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimulationNodeButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
