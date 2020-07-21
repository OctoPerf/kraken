package com.octoperf.kraken.analysis.server.service;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.entity.StorageNode;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;

public interface AnalysisService {

  Mono<StorageNode> create(Owner owner, Result result);

  Mono<String> delete(Owner owner, String resultId);

  Mono<StorageNode> setStatus(Owner owner, String resultId, ResultStatus status);

  Mono<DebugEntry> addDebug(Owner owner, DebugEntry debug);

  Mono<ResponseCookie> grafanaLogin(Owner owner);

}

