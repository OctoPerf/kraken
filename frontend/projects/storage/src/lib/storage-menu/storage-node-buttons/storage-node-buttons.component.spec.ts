import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {StorageNodeButtonsComponent} from './storage-node-buttons.component';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('StorageNodeButtonsComponent', () => {
  let component: StorageNodeButtonsComponent;
  let fixture: ComponentFixture<StorageNodeButtonsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [StorageNodeButtonsComponent],
      providers: [
        {provide: STORAGE_NODE, useValue: testStorageFileNode()}
      ]
    })
      .overrideTemplate(StorageNodeButtonsComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageNodeButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
