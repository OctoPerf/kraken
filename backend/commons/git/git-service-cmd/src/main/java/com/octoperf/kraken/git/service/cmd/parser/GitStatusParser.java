package com.octoperf.kraken.git.service.cmd.parser;

import com.octoperf.kraken.git.entity.GitStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface GitStatusParser extends Function<Flux<String>, Mono<GitStatus>> {

}
