import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {SecurityInterceptor} from 'projects/security/src/lib/security-interceptor.service';

@NgModule({
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: SecurityInterceptor, multi: true},
  ]
})
export class SecurityModule { }
