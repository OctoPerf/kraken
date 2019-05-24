import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteCommandDialogComponent} from './execute-command-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {Command} from 'projects/command/src/lib/entities/command';
import SpyObj = jasmine.SpyObj;
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {FormBuilder} from '@angular/forms';
import {CommandDialogsModule} from 'projects/command/src/lib/command-dialogs/command-dialogs.module';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';

describe('ExecuteCommandDialogComponent shell', () => {
  let component: ExecuteCommandDialogComponent;
  let fixture: ComponentFixture<ExecuteCommandDialogComponent>;
  let dialogRef: MatDialogRef<ExecuteCommandDialogComponent>;

  const shellCommand = new Command([Command.SHELL_0, Command.SHELL_1, 'ls'], {KEY: 'value'});

  beforeEach(async(() => {
    dialogRef = dialogRefSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule, CommandDialogsModule],
      providers: [
        {provide: MatDialogRef, useValue: dialogRef},
        {provide: MAT_DIALOG_DATA, useValue: {command: shellCommand, path: 'path'}},
      ]
    })
    // .overrideTemplate(ExecuteCommandDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteCommandDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.selectedIndex).toBe(ExecuteCommandDialogComponent.SHELL_INDEX);
    expect(component.envExpanded).toBeTruthy();
    expect(component.envForm).toBeDefined();
    expect(component.shellForm).toBeDefined();
    expect(component.advancedForm).toBeDefined();
  });

  it('should execute', () => {
    component.execute();
    expect(dialogRef.close).toHaveBeenCalledWith(shellCommand);
  });

  it('should execute advanced', () => {
    component.selectedIndex = ExecuteCommandDialogComponent.ADVANCED_INDEX;
    component.execute();
    expect(dialogRef.close).toHaveBeenCalledWith(shellCommand);
  });

  it('should isValid', () => {
    expect(component.isValid()).toBeTruthy();
  });

  it('should isValid false', () => {
    component.getKey(0).setValue(null);
    expect(component.isValid()).toBeFalsy();
  });

  it('should return shellCommand', () => {
    expect(component.shellCommand).toBeDefined();
  });

  it('should return command', () => {
    expect(component.commands.length).toBe(3);
  });

  it('should addCommand', () => {
    component.addCommand();
    expect(component.commands.length).toBe(4);
  });

  it('should remove command', () => {
    component.removeCommand(0);
    expect(component.commands.length).toBe(2);
  });

  it('should return command', () => {
    expect(component.getCommand(0)).toBeDefined();
  });

  it('should return variables', () => {
    expect(component.variables.length).toBe(1);
  });

  it('should addVariable', () => {
    component.addVariable();
    expect(component.variables.length).toBe(2);
  });

  it('should remove variable', () => {
    component.removeVariable(0);
    expect(component.variables.length).toBe(0);
  });

  it('should return key', () => {
    expect(component.getKey(0)).toBeDefined();
  });

  it('should return value', () => {
    expect(component.getValue(0)).toBeDefined();
  });
});
