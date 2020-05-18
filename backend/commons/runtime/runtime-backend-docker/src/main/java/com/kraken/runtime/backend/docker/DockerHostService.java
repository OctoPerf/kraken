package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.backend.api.HostService;
import com.kraken.runtime.entity.host.Host;
import com.kraken.runtime.entity.host.HostCapacity;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerHostService implements HostService {

  private static final String EMPTY = "-";

  @Override
  public Flux<Host> list(final Owner owner) {
    return Flux.just(Host.builder()
        .id("local")
        .name("local")
        .capacity(HostCapacity.builder().cpu(EMPTY).memory(EMPTY).build())
        .allocatable(HostCapacity.builder().cpu(EMPTY).memory(EMPTY).build())
        .addresses(ImmutableList.of())
        .owner(owner)
        .build());
  }

  @Override
  public Flux<Host> listAll() {
    return Flux.empty();
  }

  @Override
  public Mono<Host> detach(final Host host) {
    return Mono.error(new UnsupportedOperationException("Cannot detach host"));
  }

  @Override
  public Mono<Host> attach(Host host) {
    return Mono.error(new UnsupportedOperationException("Cannot attach host"));
  }

}
