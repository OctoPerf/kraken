package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.tools.webclient.Client;
import reactor.core.publisher.Mono;

public interface GrafanaUserClient extends Client {

  Mono<String> importDashboard(String testId,
                               Long folderId,
                               String title,
                               Long startDate,
                               String dashboard);

  Mono<String> updateDashboard(String testId,
                               Long folderId,
                               Long endDate);

  Mono<String> deleteDashboard(String testId);

  Mono<Long> createDatasource();

  Mono<Long> createFolder(String uid, String title);

  Mono<Long> getFolderId(String uid);

  Mono<String> deleteFolder(String uid);

}
