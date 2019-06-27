import {Component, ViewChild} from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'lib-contextual-menu',
  templateUrl: './contextual-menu.component.html',
  styleUrls: ['./contextual-menu.component.scss']
})
export class ContextualMenuComponent {

  contextMenuPosition = {x: 0, y: 0};

  @ViewChild(MatMenuTrigger, { static: true })
  contextMenu: MatMenuTrigger;

  open(event: MouseEvent) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX;
    this.contextMenuPosition.y = event.clientY;
    this.contextMenu.openMenu();
    document.getElementsByClassName('cdk-overlay-backdrop')[0].addEventListener('contextmenu', (offEvent: any) => {
      offEvent.preventDefault();
      this.contextMenu.closeMenu();
    });
  }
}
