import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HostsTableComponent} from './hosts-table.component';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {testHost, testHosts} from 'projects/runtime/src/lib/entities/host.spec';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {AttachHostDialogComponent} from 'projects/runtime/src/lib/runtime-host/runtime-host-dialogs/attach-host-dialog/attach-host-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {testTask} from 'projects/runtime/src/lib/entities/task.spec';

describe('HostsTableComponent', () => {
  let component: HostsTableComponent;
  let fixture: ComponentFixture<HostsTableComponent>;
  let hostService: SpyObj<RuntimeHostService>;
  let dialogs: SpyObj<DialogService>;

  beforeEach(async(() => {
    dialogs = dialogsServiceSpy();
    hostService = runtimeHostServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [HostsTableComponent],
      providers: [
        {provide: RuntimeHostService, useValue: hostService},
        {provide: DialogService, useValue: dialogs},
      ]
    })
      .overrideTemplate(HostsTableComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init', () => {
    spyOn(component, 'refresh');
    component.ngOnInit();
    expect(component.refresh).toHaveBeenCalled();
  });

  it('should refresh', () => {
    hostService.all.and.returnValue(of(testHosts()));
    component.refresh();
    expect(component.loading).toBe(true);
    expect(hostService.all).toHaveBeenCalled();
  });

  it('should set hosts', () => {
    const hosts = testHosts();
    hostService.allSubject.next(hosts);
    expect(component.dataSource.data).toBe(hosts);
  });

  it('should setId', () => {
    const host = testHost();
    dialogs.open.and.returnValue(of(host.id));
    hostService.attach.and.returnValue(of(host));
    component.setId(host);
    expect(dialogs.open).toHaveBeenCalledWith(
      AttachHostDialogComponent, DialogSize.SIZE_MD, {
        title: `Set Host ID for '${host.name}'`,
        initialId: host.id
      }
    );
    expect(hostService.attach).toHaveBeenCalledWith(host, host.id);
  });

  it('should attach', () => {
    const host = testHost();
    dialogs.open.and.returnValue(of(host.id));
    hostService.attach.and.returnValue(of(host));
    component.attach(host);
    expect(dialogs.open).toHaveBeenCalledWith(
      AttachHostDialogComponent, DialogSize.SIZE_MD, {
        title: `Attach Host '${host.name}'`,
        initialId: ''
      }
    );
    expect(hostService.attach).toHaveBeenCalledWith(host, host.id);
  });

  it('should detach', () => {
    const host = testHost();
    dialogs.confirm.and.returnValue(of(null));
    hostService.detach.and.returnValue(of(host));
    component.detach(host, true);
    expect(dialogs.confirm).toHaveBeenCalledWith('Detach Host', 'This host will not be usable to execute tasks on Kraken. Are you sure?', true);
    expect(hostService.detach).toHaveBeenCalledWith(host);
  });

  it('should _onEnter no selection', () => {
    expect(component._onEnter(null)).toBeFalse();
  });

  it('should _onEnter setId', () => {
    component.selection = testHost();
    const spy = spyOn(component, 'setId');
    expect(component._onEnter(null)).toBeTrue();
    expect(spy).toHaveBeenCalled();
  });

  it('should _onEnter attach', () => {
    const host = testHost();
    host.id = '';
    component.selection = host;
    const spy = spyOn(component, 'attach');
    expect(component._onEnter(null)).toBeTrue();
    expect(spy).toHaveBeenCalled();
  });
});
