package com.kraken.grafana.client.api;

import com.kraken.tools.webclient.Client;
import reactor.core.publisher.Mono;

public interface GrafanaUserClient extends Client {

  Mono<String> importDashboard(String testId,
                               String title,
                               Long startDate,
                               String dashboard);

  Mono<String> updateDashboard(String testId,
                               Long endDate);

  Mono<String> deleteDashboard(String testId);

  Mono<Long> createDatasource();


}
