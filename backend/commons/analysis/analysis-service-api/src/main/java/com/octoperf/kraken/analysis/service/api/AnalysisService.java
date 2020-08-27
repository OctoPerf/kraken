package com.octoperf.kraken.analysis.service.api;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.entity.StorageNode;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;

public interface AnalysisService {

  Mono<StorageNode> createResult(Owner owner, Result result);

  Mono<String> deleteResult(Owner owner, String resultId);

  Mono<StorageNode> setResultStatus(Owner owner, String resultId, ResultStatus status);

  Mono<DebugEntry> addDebugEntry(Owner owner, DebugEntry debug);

  Mono<ResponseCookie> grafanaLogin(Owner owner);

}

