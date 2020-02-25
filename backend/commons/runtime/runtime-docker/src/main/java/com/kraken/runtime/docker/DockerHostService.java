package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.HostService;
import com.kraken.runtime.entity.Host;
import com.kraken.runtime.entity.HostCapacity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class DockerHostService implements HostService {

  private static final String EMPTY = "-";

  @Override
  public Flux<Host> list() {
    return Flux.just(Host.builder()
        .id("local")
        .name("local")
        .capacity(HostCapacity.builder().cpu(EMPTY).memory(EMPTY).build())
        .allocatable(HostCapacity.builder().cpu(EMPTY).memory(EMPTY).build())
        .addresses(ImmutableList.of())
        .build());
  }

}
