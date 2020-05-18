import {Component} from '@angular/core';
import {of} from 'rxjs';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private http: HttpClient,
              private window: WindowService) {
  }

  public open() {
    const login = this.http.post('http://localhost:8300/grafana/login', {'user': 'admin', 'password': 'kraken', 'email': ''});
    this.window.open(login.pipe(map(value => 'http://localhost:8300/grafana/')));
  }
}
