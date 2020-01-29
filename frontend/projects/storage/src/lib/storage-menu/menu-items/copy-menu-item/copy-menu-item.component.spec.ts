import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CopyMenuItemComponent} from './copy-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

describe('CopyMenuItemComponent', () => {
  let component: CopyMenuItemComponent;
  let fixture: ComponentFixture<CopyMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CopyMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: CopyPasteService, useValue: copyPasteServiceSpy()},
        {provide: STORAGE_ID, useValue: 'storage'},
      ]
    })
      .overrideTemplate(CopyMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should copy key binding', () => {
    const binding = component.binding;
    expect(binding.keys).toEqual(['ctrl + c']);
    spyOn(component, '_handleKey');
    binding.binding(null);
    expect(component._handleKey).toHaveBeenCalledWith(jasmine.any(Function));
  });
});
