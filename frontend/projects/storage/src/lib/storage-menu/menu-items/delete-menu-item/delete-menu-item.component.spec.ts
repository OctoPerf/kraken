import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteMenuItemComponent} from './delete-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('DeleteMenuItemComponent', () => {
  let component: DeleteMenuItemComponent;
  let fixture: ComponentFixture<DeleteMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteMenuItemComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: STORAGE_ID, useValue: 'storage'},
      ]
    })
      .overrideTemplate(DeleteMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete key binding', () => {
    const binding = component.binding;
    expect(binding.keys).toEqual(['Delete']);
    spyOn(component, '_handleKey');
    binding.binding(null);
    expect(component._handleKey).toHaveBeenCalledWith(jasmine.any(Function));
  });
});
