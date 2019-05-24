import { QueryParamsToStringPipe } from './query-params-to-string.pipe';

describe('QueryParamsToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new QueryParamsToStringPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform()).toBe('');
    expect(pipe.transform({})).toBe('');
    expect(pipe.transform({key: 'value'})).toBe('?key=value');
    expect(pipe.transform({key1: 'value1', key2: 'value2'})).toBe('?key1=value1&key2=value2');
  });
});
