import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {SecurityInterceptor} from 'projects/security/src/lib/security-interceptor.service';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CommonModule} from '@angular/common';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {AccountMenuComponent} from './account-menu/account-menu.component';

@NgModule({
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: SecurityInterceptor, multi: true},
  ],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule
  ],
  declarations: [AccountMenuComponent],
  exports: [AccountMenuComponent]
})
export class SecurityModule {
}
