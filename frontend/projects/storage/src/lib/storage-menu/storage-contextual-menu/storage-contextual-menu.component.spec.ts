import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StorageContextualMenuComponent} from './storage-contextual-menu.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {ContextualMenuEvent} from 'projects/storage/src/lib/events/contextual-menu-event';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('StorageContextualMenuComponent', () => {
  let component: StorageContextualMenuComponent;
  let fixture: ComponentFixture<StorageContextualMenuComponent>;
  let eventBus: EventBusService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StorageContextualMenuComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: CopyPasteService, useValue: copyPasteServiceSpy()},
        {provide: STORAGE_ID, useValue: 'storage-id'},
      ]
    })
      .overrideTemplate(StorageContextualMenuComponent, '')
      .compileComponents();
    eventBus = TestBed.inject(EventBusService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageContextualMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should open menu', () => {
    component._contextualMenu = jasmine.createSpyObj('contextualMenu', ['open']);
    eventBus.publish(new ContextualMenuEvent(null, 'storage-id'));
    expect(component._contextualMenu.open).toHaveBeenCalled();
  });
});
