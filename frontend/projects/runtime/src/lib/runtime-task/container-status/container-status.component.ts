import {Component, Input, OnInit} from '@angular/core';
import {ContainerStatus} from 'projects/runtime/src/lib/entities/container-status';

@Component({
  selector: 'lib-container-status',
  templateUrl: './container-status.component.html',
  styleUrls: ['./container-status.component.scss']
})
export class ContainerStatusComponent {

  private static readonly VALUES: { [key in ContainerStatus]: number } = {
    CREATING: 0,
    STARTING: 10,
    PREPARING: 25,
    READY: 40,
    RUNNING: 60,
    STOPPING: 80,
    DONE: 100,
    FAILED: 100
  };

  private _status: ContainerStatus;
  public value: number;

  @Input()
  set status(status: ContainerStatus) {
    this._status = status;
    this.value = ContainerStatusComponent.VALUES[status];
  }

  get status(): ContainerStatus {
    return this._status;
  }
}
