import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DeleteMenuItemComponent} from './delete-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('DeleteMenuItemComponent', () => {
  let component: DeleteMenuItemComponent;
  let fixture: ComponentFixture<DeleteMenuItemComponent>;

  beforeEach(waitForAsync(() => {
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
    expect(binding.keys).toEqual(['Delete', 'ctrl + Delete']);
    spyOn(component, '_handleKey');
    binding.binding({ctrlKey: true} as any);
    expect(component._handleKey).toHaveBeenCalledWith(jasmine.any(Function));
  });
});
