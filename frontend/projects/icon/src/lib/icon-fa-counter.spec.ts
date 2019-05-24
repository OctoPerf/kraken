import {faAsterisk} from '@fortawesome/free-solid-svg-icons/faAsterisk';
import {IconFa} from './icon-fa';
import {IconFaCounter} from 'projects/icon/src/lib/icon-fa-counter';

describe('IconFaCounter', () => {

  it('should new', () => {
    expect(new IconFaCounter(new IconFa(faAsterisk))).toBeDefined();
  });

});
