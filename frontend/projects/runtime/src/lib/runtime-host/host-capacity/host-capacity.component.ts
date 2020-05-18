import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'lib-host-capacity',
  templateUrl: './host-capacity.component.html',
  styleUrls: ['./host-capacity.component.scss']
})
export class HostCapacityComponent implements OnInit {

  @Input()
  allocatable: string;
  @Input()
  capacity: string;
  progress = 0;
  color: string;

  ngOnInit() {
    const capacity = parseFloat(this.capacity);
    const allocatable = parseFloat(this.allocatable);
    if (!isNaN(capacity) && !isNaN(allocatable)) {
      this.progress = 10 + (allocatable * 90 / capacity);
      this.color = this.progress > 50 ? 'accent' : 'warn';
    }
  }

}
