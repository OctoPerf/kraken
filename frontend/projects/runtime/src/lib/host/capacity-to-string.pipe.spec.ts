import { CapacityToStringPipe } from './capacity-to-string.pipe';

describe('CapacityToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new CapacityToStringPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform({foo: 'bar', hello: 'world'}))
      .toEqual('foo=bar, hello=world');
  });
});
