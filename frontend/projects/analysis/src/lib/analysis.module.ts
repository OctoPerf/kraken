import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AnalysisApplicationIdService} from 'projects/analysis/src/lib/analysis-application-id.service';

@NgModule({
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AnalysisApplicationIdService, multi: true},
  ]
})
export class AnalysisModule { }
