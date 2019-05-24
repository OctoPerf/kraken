import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {Color} from 'projects/color/src/lib/color';
import {ColorToFillClassPipe} from 'projects/color/src/lib/color-to-fill-class.pipe';

@Component({
  selector: 'lib-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {

  @Input() color: Color = 'primary';
  @Input() size = 24;

  @HostBinding('class')
  get hostClass(): string {
    return this.toFill.transform(this.color);
  }

  constructor(private toFill: ColorToFillClassPipe) {
  }

  ngOnInit() {
  }

}
