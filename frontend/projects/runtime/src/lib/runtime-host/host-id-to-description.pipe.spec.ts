import {HostIdToDescriptionPipe} from './host-id-to-description.pipe';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {AddressesToStringPipe} from 'projects/runtime/src/lib/runtime-host/addresses-to-string.pipe';
import {CapacityToStringPipe} from 'projects/runtime/src/lib/runtime-host/capacity-to-string.pipe';
import {testHost} from 'projects/runtime/src/lib/entities/host.spec';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import SpyObj = jasmine.SpyObj;

describe('HostIdToDescriptionPipe', () => {

  let pipe: HostIdToDescriptionPipe;
  let hostService: SpyObj<RuntimeHostService>;

  beforeEach(() => {
    hostService = runtimeHostServiceSpy();
    pipe = new HostIdToDescriptionPipe(hostService, new AddressesToStringPipe(), new CapacityToStringPipe());
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('create convert host', () => {
    const host = testHost();
    hostService.host.and.returnValue(host);
    expect(pipe.transform(host.id)).toEqual('Addresses: type=address - Capacity: key=value');
    expect(hostService.host).toHaveBeenCalledWith(host.id);
  });

  it('create convert not found', () => {
    const id = 'id';
    hostService.host.and.returnValue(undefined);
    expect(pipe.transform(id)).toEqual(id);
    expect(hostService.host).toHaveBeenCalledWith(id);
  });
});
