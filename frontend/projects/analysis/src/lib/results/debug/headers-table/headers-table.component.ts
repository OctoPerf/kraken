import {Component, Input, OnInit, ViewChild} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import * as _ from 'lodash';
import {HttpHeader} from 'projects/analysis/src/lib/entities/http-header';

@Component({
  selector: 'lib-headers-table',
  templateUrl: './headers-table.component.html',
  styleUrls: ['./headers-table.component.scss']
})
export class HeadersTableComponent implements OnInit {

  readonly displayedColumns: string[] = ['key', 'value'];

  dataSource: MatTableDataSource<{ key: string, value: string }>;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @Input() headers: HttpHeader[];

  constructor() {
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.headers);
    this.dataSource.sort = this.sort;
  }

}
