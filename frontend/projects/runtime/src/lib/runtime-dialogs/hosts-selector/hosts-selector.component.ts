import {Component, Input, OnInit} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {FormGroup} from '@angular/forms';
import {Host} from 'projects/runtime/src/lib/entities/host';

@Component({
  selector: 'lib-hosts-selector',
  templateUrl: './hosts-selector.component.html',
  styleUrls: ['./hosts-selector.component.scss']
})
export class HostsSelectorComponent implements OnInit {


  @Input() formGroup: FormGroup;
  @Input() multiple: boolean;

  public hosts: Host[];

  constructor(private hostService: RuntimeHostService) {
  }

  ngOnInit() {
    this.hosts = this.hostService.hostsSubject.value;
  }

}
