import {Component} from '@angular/core';
import {faUserShield} from '@fortawesome/free-solid-svg-icons/faUserShield';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faUserShield);

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {

}
