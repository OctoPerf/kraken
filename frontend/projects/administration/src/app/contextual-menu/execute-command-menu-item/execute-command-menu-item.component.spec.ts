import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteCommandMenuItemComponent} from './execute-command-menu-item.component';
import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('ExecuteCommandMenuItemComponent', () => {
  let component: ExecuteCommandMenuItemComponent;
  let fixture: ComponentFixture<ExecuteCommandMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ExecuteCommandMenuItemComponent],
      providers: [
        {provide: CommandService, useValue: commandServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(ExecuteCommandMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteCommandMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
