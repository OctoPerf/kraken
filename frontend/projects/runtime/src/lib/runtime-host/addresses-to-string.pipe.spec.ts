import {AddressesToStringPipe} from './addresses-to-string.pipe';

describe('AddressesToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new AddressesToStringPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform([{type: 'type1', address: 'address1'}, {type: 'type2', address: 'address2'}]))
      .toEqual('type1=address1, type2=address2');
  });
});
