import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Host} from 'projects/runtime/src/lib/entities/host';
import * as _ from 'lodash';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {testHosts} from 'projects/runtime/src/lib/entities/host.spec';

@Component({
  selector: 'lib-hosts-selector',
  templateUrl: './hosts-selector.component.html',
  styleUrls: ['./hosts-selector.component.scss']
})
export class HostsSelectorComponent implements OnInit, OnDestroy {

  private static readonly ID_PREFIX = 'host-selector-';

  @Input() storageId: string;
  @Input() formGroup: FormGroup;
  @Input() multiple: boolean;

  readonly displayedColumns: string[] = ['select', 'id', 'name', 'cpu', 'memory', 'addresses'];
  selection: SelectionModel<Host>;
  loading = true;
  dataSource: MatTableDataSource<Host> = new MatTableDataSource([]);
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  private subscription: Subscription;

  constructor(private hostService: RuntimeHostService,
              private localStorage: LocalStorageService) {
  }

  ngOnInit() {
    this.formGroup.addControl('hosts', new FormControl(this.multiple ? [] : null, [Validators.required]));
    this.selection = new SelectionModel(this.multiple);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

    this.hostService.hosts().subscribe(hosts => {
      this.loading = false;
      this.dataSource.data = hosts;
      this.subscription = this.selection.changed.subscribe(this.selectionChanged.bind(this));

      const hostIds = _.map(hosts, 'id');
      const savedIds = this.localStorage.getItem<string[]>(HostsSelectorComponent.ID_PREFIX + this.storageId, hostIds);
      const selectedHosts = _.filter(hosts, (host: Host) => savedIds.indexOf(host.id) !== -1);
      this.multiple ? this.selection.select(...selectedHosts) : this.selection.select(_.first(selectedHosts));
    });
  }

  private selectionChanged() {
    const ids = _.map(this.selection.selected, 'id');
    this.hosts.setValue(this.multiple ? ids : _.first(ids));
    this.localStorage.setItem(HostsSelectorComponent.ID_PREFIX + this.storageId, ids);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  get hosts(): AbstractControl {
    return this.formGroup.get('hosts');
  }

  get hostIds(): string[] {
    return this.multiple ? this.hosts.value : [this.hosts.value];
  }

  get hostId(): string {
    return this.hosts.value;
  }

}
