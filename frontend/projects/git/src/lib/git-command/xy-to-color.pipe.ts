import {Pipe, PipeTransform} from '@angular/core';
import {Color} from 'projects/color/src/lib/color';

@Pipe({
  name: 'xyToColor',
  pure: true,
})
export class XyToColorPipe implements PipeTransform {

  public static VALUES: Map<string, Color> = new Map<string, Color>([
    ['.', 'foreground'],
    ['!', 'muted'],
    ['?', 'error'],
    ['M', 'info'],
    ['A', 'success'],
    ['D', 'error'],
    ['R', 'info'],
    ['C', 'success'],
    ['U', 'info'],
  ]);

  public static xyToColor(xY: string): Color {
    return XyToColorPipe.VALUES.get(xY) || 'foreground';
  }

  transform(xy: string): Color {
    const index = xy.charAt(0);
    const workingTree = xy.charAt(1);
    const indexLabel = XyToColorPipe.xyToColor(index);
    const workingTreeLabel = XyToColorPipe.xyToColor(workingTree);
    return workingTreeLabel !== 'foreground' ? workingTreeLabel : indexLabel;
  }

}
