package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.Host;
import com.kraken.runtime.entity.HostCapacity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerHostServiceTest {

  DockerHostService service;

  @Before
  public void before() {
    service = new DockerHostService();
  }

  @Test
  public void shouldList() {

    final var list = service.list().collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0)).isEqualTo(Host.builder()
        .id("local")
        .name("local")
        .capacity(HostCapacity.builder().cpu("-").memory("-").build())
        .allocatable(HostCapacity.builder().cpu("-").memory("-").build())
        .addresses(ImmutableList.of())
        .build());
  }

}
