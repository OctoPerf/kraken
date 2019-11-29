import {HostIdToNamePipe} from './host-id-to-name.pipe';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {testHost} from 'projects/runtime/src/lib/entities/host.spec';
import SpyObj = jasmine.SpyObj;

describe('HostIdToNamePipe', () => {

  let pipe: HostIdToNamePipe;
  let hostService: SpyObj<RuntimeHostService>;

  beforeEach(() => {
    hostService = runtimeHostServiceSpy();
    pipe = new HostIdToNamePipe(hostService);
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('create convert host', () => {
    const host = testHost();
    hostService.host.and.returnValue(host);
    expect(pipe.transform(host.id)).toEqual('name0');
    expect(hostService.host).toHaveBeenCalledWith(host.id);
  });

  it('create convert not found', () => {
    const id = 'id';
    hostService.host.and.returnValue(undefined);
    expect(pipe.transform(id)).toEqual(id);
    expect(hostService.host).toHaveBeenCalledWith(id);
  });
});
