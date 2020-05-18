import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AnalysisApplicationIdInterceptorService} from 'projects/analysis/src/lib/analysis-application-id-interceptor.service';

@NgModule({
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AnalysisApplicationIdInterceptorService, multi: true},
  ]
})
export class AnalysisModule { }
