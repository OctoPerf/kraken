import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'lib-cookies-table',
  templateUrl: './cookies-table.component.html',
  styleUrls: ['./cookies-table.component.scss']
})
export class CookiesTableComponent implements OnInit {

  readonly displayedColumns: string[] = ['cookie'];

  dataSource: MatTableDataSource<string>;
  @ViewChild(MatSort) sort: MatSort;
  @Input() cookies: string[];

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.cookies);
    this.dataSource.sort = this.sort;
  }

}
