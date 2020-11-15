import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'xyToStatus',
  pure: true,
})
export class XyToStatusPipe implements PipeTransform {

  private static VALUES: { [key in string]: string } = {
    '.': 'Unmodified',
    'M': 'Modified',
    'A': 'Added',
    'D': 'Deleted',
    'R': 'Renamed',
    'C': 'Copied',
    'U': 'Updated',
  };

  transform(xy: string): string {
    const index = xy[0];
    const workingTree = xy[1];
    const indexLabel = XyToStatusPipe.VALUES[index] || index;
    const workingTreeLabel = XyToStatusPipe.VALUES[workingTree] || workingTree;
    return `${indexLabel}/${workingTreeLabel}`;
  }

}
