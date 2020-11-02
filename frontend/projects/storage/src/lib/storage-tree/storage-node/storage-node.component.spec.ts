import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {STORAGE_NODE_BUTTONS, StorageNodeComponent} from './storage-node.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageNodeButtonsComponent} from 'projects/storage/src/lib/storage-menu/storage-node-buttons/storage-node-buttons.component';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {GitFileStatusEvent} from 'projects/git/src/lib/events/git-file-status-event';

describe('StorageNodeComponent', () => {
  let component: StorageNodeComponent;
  let fixture: ComponentFixture<StorageNodeComponent>;
  let eventBus: EventBusService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [StorageNodeComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: STORAGE_NODE_BUTTONS, useValue: StorageNodeButtonsComponent},
      ]
    })
      .overrideTemplate(StorageNodeComponent, '')
      .compileComponents();

    eventBus = TestBed.inject(EventBusService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set and get node', () => {
    const node = testStorageFileNode();
    component.node = node;
    expect(component.hasChild).toBeFalse();
    expect(component.node).toBe(node);
    expect(component.hasChild).toBeFalse();
  });

  it('should set true and get hover', () => {
    component.hover = true;
    expect(component.hover).toBeTrue();
    expect(component.nodeButtons).toBeDefined();
  });

  it('should set false and get hover', () => {
    component.nodeButtons = null;
    component.hover = false;
    expect(component.hover).toBeFalse();
  });

  it('should set false and get hover (not attached)', () => {
    const nodeButtons = jasmine.createSpyObj('nodeButtons', ['detach']);
    nodeButtons.isAttached = false;
    component.nodeButtons = nodeButtons;
    component.hover = false;
    expect(component.hover).toBeFalse();
    expect(nodeButtons.detach).not.toHaveBeenCalled();
  });

  it('should set false and get hover (attached)', () => {
    const nodeButtons = jasmine.createSpyObj('nodeButtons', ['detach']);
    nodeButtons.isAttached = true;
    component.nodeButtons = nodeButtons;
    component.hover = false;
    expect(component.hover).toBeFalse();
    expect(nodeButtons.detach).toHaveBeenCalled();
  });

  it('should handle git file status event', () => {
    component.node = testStorageFileNode();
    eventBus.publish(new GitFileStatusEvent(component.node.path, 'AM', 'info'));
    expect(component.color).toBe('info');
  });
});


