import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'lib-git-path-table',
  templateUrl: './git-path-table.component.html',
  styleUrls: ['./git-path-table.component.scss']
})
export class GitPathTableComponent implements OnInit {

  public readonly displayedColumns: string[] = ['path'];
  dataSource: MatTableDataSource<string>;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  @Input()
  set paths(paths: string[]) {
    this.dataSource = new MatTableDataSource(paths);
    this.dataSource.sort = this.sort;
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource([]);
    this.dataSource.sort = this.sort;
  }
}
