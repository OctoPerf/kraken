package com.octoperf.kraken.tools.sse.server;


import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SSEChannelBuilder extends Supplier<SSEChannel>, Function<AuthenticatedClientBuildOrder, Mono<Map<String, Flux<? extends Object>>>> {
}
