import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'lib-git-file-status-table',
  templateUrl: './git-file-status-table.component.html',
  styleUrls: ['./git-file-status-table.component.scss']
})
export class GitFileStatusTableComponent implements OnInit {

  public readonly displayedColumns: string[] = ['path', 'xy'];
  dataSource: MatTableDataSource<GitFileStatus>;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  @Input()
  set fileStatuses(statuses: GitFileStatus[]) {
    this.dataSource = new MatTableDataSource(statuses);
    this.dataSource.sort = this.sort;
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource([]);
    this.dataSource.sort = this.sort;
  }
}
