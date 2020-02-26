import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {testHost} from 'projects/runtime/src/lib/entities/host.spec';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {HostIdToHostPipe} from 'projects/runtime/src/lib/runtime-host/host-id-to-host.pipe';
import SpyObj = jasmine.SpyObj;

describe('HostIdToHostPipe', () => {

  let pipe: HostIdToHostPipe;
  let hostService: SpyObj<RuntimeHostService>;

  beforeEach(() => {
    hostService = runtimeHostServiceSpy();
    pipe = new HostIdToHostPipe(hostService);
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should convert id', () => {
    const host = testHost();
    hostService.host.and.returnValue(host);
    expect(pipe.transform(host.id)).toBe(host);
  });

});
