import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import SpyObj = jasmine.SpyObj;
import {RuntimeLogService} from 'projects/runtime/src/lib/runtime-log/runtime-log.service';
import {runtimeLogServiceSpy} from 'projects/runtime/src/lib/runtime-log/runtime-log.service.spec';
import {RuntimeLogsHeaderComponent} from 'projects/runtime/src/lib/runtime-log/runtime-logs-header/runtime-logs-header.component';

describe('RuntimeLogsHeaderComponent', () => {
  let component: RuntimeLogsHeaderComponent;
  let fixture: ComponentFixture<RuntimeLogsHeaderComponent>;
  let service: SpyObj<RuntimeLogService>;

  beforeEach(async(() => {
    service = runtimeLogServiceSpy();

    TestBed.configureTestingModule({
      declarations: [RuntimeLogsHeaderComponent],
      providers: [
        {provide: RuntimeLogService, useValue: service}
      ]
    })
      .overrideTemplate(RuntimeLogsHeaderComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuntimeLogsHeaderComponent);
    component = fixture.componentInstance;
  });

  it('should update label', () => {
    service.label.and.returnValue({name: 'commandName', title: 'commandTitle'});
    component.log = {applicationId: 'test', status: 'RUNNING', id: 'id', text: '', type: 'TASK'};
    fixture.detectChanges();
    expect(component.name).toBe('commandName');
    expect(component.title).toBe('commandTitle');
  });

  it('should update label with id', () => {
    service.label.and.returnValue(undefined);
    component.log = {applicationId: 'test', status: 'RUNNING', id: 'id', text: '', type: 'TASK'};
    fixture.detectChanges();
    expect(component.name).toBe('id');
    expect(component.title).toBe('id');
  });

  it('should update label only once', () => {
    component.log = {applicationId: 'test', status: 'RUNNING', id: 'id', text: '', type: 'TASK'};
    service.label.and.returnValue({name: 'commandName', title: 'commandTitle'});
    fixture.detectChanges();
    expect(component.name).toBe('commandName');
    expect(component.title).toBe('commandTitle');

    // Labels should only be updated once
    service.label.and.returnValue(undefined);
    service.logLabelsChanged.emit();
    expect(component.name).toBe('commandName');
    expect(component.title).toBe('commandTitle');
  });

  it('should remove logs name', () => {
    service.label.and.returnValue({name: 'name', title: 'title'});
    component.log = {applicationId: 'test', status: 'RUNNING', id: 'id', text: '', type: 'TASK'};
    fixture.detectChanges();
    const unsubscribe = spyOn(component._subscription, 'unsubscribe');
    component.ngOnDestroy();
    expect(service.removeLabel).toHaveBeenCalled();
    expect(unsubscribe).toHaveBeenCalled();
  });
});
