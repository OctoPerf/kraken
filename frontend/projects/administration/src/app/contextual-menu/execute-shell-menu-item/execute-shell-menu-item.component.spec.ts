import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteShellMenuItemComponent} from './execute-shell-menu-item.component';
import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';

describe('ExecuteShellMenuItemComponent', () => {
  let component: ExecuteShellMenuItemComponent;
  let fixture: ComponentFixture<ExecuteShellMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ExecuteShellMenuItemComponent],
      providers: [
        {provide: CommandService, useValue: commandServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
      ]
    })
      .overrideTemplate(ExecuteShellMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteShellMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
