import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'lib-loading-icon',
  templateUrl: './loading-icon.component.html',
  styleUrls: ['./loading-icon.component.scss']
})
export class LoadingIconComponent implements OnInit {

  @Input() loading: boolean;

  constructor() {
  }

  ngOnInit() {
  }

}
