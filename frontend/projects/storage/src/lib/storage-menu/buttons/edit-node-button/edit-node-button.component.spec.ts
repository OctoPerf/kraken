import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EditNodeButtonComponent} from './edit-node-button.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';

describe('EditNodeButtonComponent', () => {
  let component: EditNodeButtonComponent;
  let fixture: ComponentFixture<EditNodeButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EditNodeButtonComponent],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()}
      ]
    })
      .overrideTemplate(EditNodeButtonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
