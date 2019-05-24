import {Color} from 'projects/color/src/lib/color';
import {Icon} from 'projects/icon/src/lib/icon';
import {IconFa} from 'projects/icon/src/lib/icon-fa';

export class IconFaCounter implements Icon {

  public readonly '@type' = 'IconFaCounter';

  constructor(
    public readonly icon: IconFa,
    public content: string = '',
    public contentColor: Color = 'primary',
  ) {
  }
}
