import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {ApplicationIdHeaderInterceptor} from 'projects/commons/src/lib/config/application-id-header-interceptor.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

@Injectable()
export class AnalysisApplicationIdInterceptorService extends ApplicationIdHeaderInterceptor {

  constructor(configuration: ConfigurationService, analysisConfiguration: AnalysisConfigurationService) {
    super(configuration, () => [analysisConfiguration.analysisApiUrl('')]);
  }

}
