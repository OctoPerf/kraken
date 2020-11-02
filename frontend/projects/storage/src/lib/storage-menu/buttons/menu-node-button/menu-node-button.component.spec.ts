import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {MenuNodeButtonComponent} from './menu-node-button.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('MenuNodeButtonComponent', () => {
  let component: MenuNodeButtonComponent;
  let fixture: ComponentFixture<MenuNodeButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [MenuNodeButtonComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()}
      ]
    })
      .overrideTemplate(MenuNodeButtonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
