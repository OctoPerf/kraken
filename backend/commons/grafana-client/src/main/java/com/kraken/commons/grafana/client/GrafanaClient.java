package com.kraken.commons.grafana.client;

import reactor.core.publisher.Mono;

public interface GrafanaClient {
  Mono<String> getDashboard(String testId);

  Mono<String> setDashboard(String dashboard);

  Mono<String> importDashboard(String dashboard);

  Mono<String> deleteDashboard(String testId);

  String initDashboard(String testId,
                       String title,
                       Long startDate,
                       String dashboard);

  String updatedDashboard(Long endDate,
                          String dashboard);

}
