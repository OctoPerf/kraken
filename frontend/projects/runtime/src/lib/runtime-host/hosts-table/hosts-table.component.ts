import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {MatSort} from '@angular/material/sort';
import {Subscription} from 'rxjs';
import {REFRESH_ICON} from 'projects/icon/src/lib/icons';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@Component({
  selector: 'lib-hosts-table',
  templateUrl: './hosts-table.component.html',
  styleUrls: ['./hosts-table.component.scss']
})
export class HostsTableComponent implements OnInit, OnDestroy {

  readonly refreshIcon = REFRESH_ICON;
  readonly displayedColumns: string[] = ['id', 'name', 'addresses', 'cpu', 'memory'];
  loading = true;
  dataSource: MatTableDataSource<Host>;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  private subscription: Subscription;

  constructor(private hostService: RuntimeHostService) {
    this.dataSource = new MatTableDataSource([]);
    this.subscription = this.hostService.hostsSubject.subscribe((hosts) => this.hosts = hosts);
  }

  ngOnInit() {
    this.refresh();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refresh() {
    this.loading = true;
    this.hostService.hosts().subscribe();
  }

  set hosts(hosts: Host[]) {
    this.dataSource = new MatTableDataSource(hosts);
    this.dataSource.sort = this.sort;
    this.loading = false;
  }

}
