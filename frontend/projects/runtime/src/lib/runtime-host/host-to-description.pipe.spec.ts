import {HostToDescriptionPipe} from 'projects/runtime/src/lib/runtime-host/host-to-description.pipe';
import {AddressesToStringPipe} from 'projects/runtime/src/lib/runtime-host/addresses-to-string.pipe';
import {testHost} from 'projects/runtime/src/lib/entities/host.spec';

describe('HostIdToDescriptionPipe', () => {

  let pipe: HostToDescriptionPipe;

  beforeEach(() => {
    pipe = new HostToDescriptionPipe(new AddressesToStringPipe());
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should convert host', () => {
    const host = testHost();
    expect(pipe.transform(host)).toEqual('Name: name0 - CPU: 5 / 10 - Memory: 5GB / 10GB - Addresses: type=address');
  });

});
