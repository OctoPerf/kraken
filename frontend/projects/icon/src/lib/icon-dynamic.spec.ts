import {faAsterisk} from '@fortawesome/free-solid-svg-icons/faAsterisk';
import {IconFa} from './icon-fa';
import {IconDynamic} from './icon-dynamic';

describe('IconDynamic', () => {

  it('should new', () => {
    expect(new IconDynamic(new IconFa(faAsterisk))).toBeDefined();
    expect(new IconDynamic(new IconFa(faAsterisk), {'test': new IconFa(faAsterisk)})).toBeDefined();
  });

});
