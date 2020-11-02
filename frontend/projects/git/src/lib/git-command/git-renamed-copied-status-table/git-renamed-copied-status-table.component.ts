import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {GitRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status';

@Component({
  selector: 'lib-git-renamed-copied-status-table',
  templateUrl: './git-renamed-copied-status-table.component.html',
  styleUrls: ['./git-renamed-copied-status-table.component.scss']
})
export class GitRenamedCopiedStatusTableComponent implements OnInit {

  public readonly displayedColumns: string[] = ['path', 'score', 'xy'];
  dataSource: MatTableDataSource<GitRenamedCopiedStatus>;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  @Input()
  set renamedCopiedStatuses(statuses: GitRenamedCopiedStatus[]) {
    this.dataSource = new MatTableDataSource(statuses);
    this.dataSource.sort = this.sort;
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource([]);
    this.dataSource.sort = this.sort;
  }
}
