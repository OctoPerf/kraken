import {Icon} from './icon';

export class IconDynamic implements Icon {

  public readonly '@type' = 'IconDynamic';

  constructor(
    public readonly defaultIcon: Icon,
    public readonly stateIcons: {[key in string]: Icon} = {},
  ) {
  }
}
