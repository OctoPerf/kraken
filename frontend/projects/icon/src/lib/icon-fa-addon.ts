import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {Icon} from 'projects/icon/src/lib/icon';

export class IconFaAddon implements Icon {

  public readonly '@type' = 'IconFaAddon';

  constructor(
    public readonly icon: IconFa,
    public readonly addon: IconFa,
  ) {
  }
}
