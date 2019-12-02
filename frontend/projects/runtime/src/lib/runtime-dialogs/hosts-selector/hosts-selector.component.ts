import {Component, Input, OnInit} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@Component({
  selector: 'lib-hosts-selector',
  templateUrl: './hosts-selector.component.html',
  styleUrls: ['./hosts-selector.component.scss']
})
export class HostsSelectorComponent implements OnInit {


  @Input() multiple: boolean;

  constructor(private hostService: RuntimeHostService) {
    hostService.hosts();
  }

  ngOnInit() {
  }

}
