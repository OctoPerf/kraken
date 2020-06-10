package com.octoperf.kraken.analysis.client.api;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.storage.entity.StorageNode;
import reactor.core.publisher.Mono;

public interface AnalysisClient extends AuthenticatedClient {

  Mono<StorageNode> create(Result result);

  Mono<String> delete(String resultId);

  Mono<StorageNode> setStatus(String resultId, ResultStatus status);

  Mono<DebugEntry> debug(DebugEntry debug);

}
