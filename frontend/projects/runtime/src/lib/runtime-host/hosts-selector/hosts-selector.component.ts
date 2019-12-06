import {Component, Input, OnInit} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Host} from 'projects/runtime/src/lib/entities/host';
import * as _ from 'lodash';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';

@Component({
    selector: 'lib-hosts-selector',
    templateUrl: './hosts-selector.component.html',
    styleUrls: ['./hosts-selector.component.scss']
})
export class HostsSelectorComponent implements OnInit {

    private static readonly ID_PREFIX = 'host-selector-';
    @Input() storageId: string;
    @Input() formGroup: FormGroup;
    @Input() multiple: boolean;

    public hostsList: Host[] = [];
    public loading = true;

    constructor(private hostService: RuntimeHostService,
                private localStorage: LocalStorageService) {
    }

    ngOnInit() {
        this.hostService.hosts().subscribe(hosts => {
            this.loading = false;
            this.hostsList = hosts;
            const hostIds = _.map(this.hostsList, 'id');
            const savedIds = this.localStorage.getItem<string[]>(HostsSelectorComponent.ID_PREFIX + this.storageId, []);
            const intersect = _.intersection(hostIds, savedIds);
            const selectedHostIds = intersect.length ? intersect : hostIds;
            const selectedHostId = _.first(hostIds);
            this.formGroup.addControl('hosts', new FormControl(this.multiple ? selectedHostIds : selectedHostId, [Validators.required]));
        });
    }

    get hosts(): AbstractControl {
        return this.formGroup.get('hosts');
    }

    get hostIds(): string[] {
        const hostIds = this.hosts ? this.multiple ? this.hosts.value : [this.hosts.value] : [];
        this.localStorage.setItem(HostsSelectorComponent.ID_PREFIX + this.storageId, hostIds);
        return hostIds;
    }

    get hostId(): string {
        const hostId = this.hosts ? this.hosts.value : null;
        this.localStorage.setItem(HostsSelectorComponent.ID_PREFIX + this.storageId, [hostId]);
        return hostId;
    }

}
