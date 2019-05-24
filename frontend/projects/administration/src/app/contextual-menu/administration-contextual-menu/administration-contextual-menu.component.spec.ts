import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AdministrationContextualMenuComponent} from './administration-contextual-menu.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('AdministrationContextualMenuComponent', () => {
  let component: AdministrationContextualMenuComponent;
  let fixture: ComponentFixture<AdministrationContextualMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdministrationContextualMenuComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()}
      ]
    })
      .overrideTemplate(AdministrationContextualMenuComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrationContextualMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
