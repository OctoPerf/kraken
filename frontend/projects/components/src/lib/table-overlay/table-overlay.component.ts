import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'lib-table-overlay',
  templateUrl: './table-overlay.component.html',
  styleUrls: ['./table-overlay.component.scss']
})
export class TableOverlayComponent implements OnInit {

  @Input() loading: boolean;
  @Input() dataLength: number;
  @Input() noDataLabel: string;

  ngOnInit(): void {
    this.noDataLabel = this.noDataLabel || 'No data';
  }

}
