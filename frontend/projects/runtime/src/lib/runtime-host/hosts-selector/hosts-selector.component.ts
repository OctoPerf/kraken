import {Component, Input, OnInit} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Host} from 'projects/runtime/src/lib/entities/host';
import * as _ from 'lodash';

@Component({
  selector: 'lib-hosts-selector',
  templateUrl: './hosts-selector.component.html',
  styleUrls: ['./hosts-selector.component.scss']
})
export class HostsSelectorComponent implements OnInit {

  @Input() formGroup: FormGroup;
  @Input() multiple: boolean;

  public hostsList: Host[] = [];
  public loading = true;

  constructor(private hostService: RuntimeHostService) {
  }

  ngOnInit() {
    this.hostService.hosts().subscribe(hosts => {
      this.loading = false;
      this.hostsList = hosts;
      const hostIds = _.map(this.hostsList, 'id');
      const hostId = _.first(hostIds);
      console.log(hostId);
      // TODO load list from storage
      this.formGroup.addControl('hosts', new FormControl(this.multiple ? hostIds : hostId, [Validators.required]));
    });
  }

  get hosts(): AbstractControl {
    return this.formGroup.get('hosts');
  }

  get hostIds(): string[] {
    // TODO save list to storage
    return this.hosts ? this.hosts.value : [];
  }

  get hostId(): string {
    // TODO save id to storage
    return this.hosts ? this.hosts.value : null;
  }

}
