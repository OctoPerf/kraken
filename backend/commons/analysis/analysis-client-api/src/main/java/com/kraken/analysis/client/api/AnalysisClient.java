package com.kraken.analysis.client.api;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.security.authentication.client.api.AuthenticatedClient;
import com.kraken.storage.entity.StorageNode;
import reactor.core.publisher.Mono;

public interface AnalysisClient extends AuthenticatedClient {

  Mono<StorageNode> create(Result result);

  Mono<String> delete(String resultId);

  Mono<StorageNode> setStatus(String resultId, ResultStatus status);

  Mono<DebugEntry> debug(DebugEntry debug);

}
