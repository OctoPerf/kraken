import {faAsterisk} from '@fortawesome/free-solid-svg-icons/faAsterisk';
import {IconFa} from './icon-fa';

describe('IconFa', () => {

  it('should new', () => {
    expect(new IconFa(faAsterisk)).toBeDefined();
    expect(new IconFa(faAsterisk, 'accent')).toBeDefined();
    expect(new IconFa(faAsterisk, 'background', 'test')).toBeDefined();
  });

});
