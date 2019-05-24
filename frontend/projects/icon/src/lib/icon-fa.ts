import {IconDefinition} from '@fortawesome/fontawesome-common-types';
import {Icon} from './icon';
import {Color} from 'projects/color/src/lib/color';

export class IconFa implements Icon {

  public readonly '@type' = 'IconFa';

  constructor(
    public readonly icon: IconDefinition,
    public readonly color: Color = 'foreground',
    public readonly transform: string = '',
    public readonly spin = false,
  ) {
  }
}
