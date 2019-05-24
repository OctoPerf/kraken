import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NewFileMenuItemComponent} from './new-file-menu-item.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('NewFileMenuItemComponent', () => {
  let component: NewFileMenuItemComponent;
  let fixture: ComponentFixture<NewFileMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NewFileMenuItemComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(NewFileMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewFileMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
