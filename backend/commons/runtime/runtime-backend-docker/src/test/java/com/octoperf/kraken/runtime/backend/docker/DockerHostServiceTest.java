package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.entity.host.Host;
import com.octoperf.kraken.runtime.entity.host.HostCapacity;
import com.octoperf.kraken.runtime.entity.host.HostTest;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerHostServiceTest {

  DockerHostService service;

  @BeforeEach
  public void before() {
    service = new DockerHostService();
  }

  @Test
  public void shouldList() {
    final var list = service.list(Owner.PUBLIC).collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0)).isEqualTo(Host.builder()
        .id("local")
        .name("local")
        .capacity(HostCapacity.builder().cpu("-").memory("-").build())
        .allocatable(HostCapacity.builder().cpu("-").memory("-").build())
        .addresses(ImmutableList.of())
        .owner(Owner.PUBLIC)
        .build());
  }

  @Test
  public void shouldListAll() {
    final var list = service.listAll().collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(0);
  }

  @Test
  public void shouldNotDetach() {
    Assertions.assertThrows(UnsupportedOperationException.class, () -> {
      service.detach(HostTest.HOST).block();
    });
  }

  @Test
  public void shouldNotAttach() {
    Assertions.assertThrows(UnsupportedOperationException.class, () -> {
      service.attach(HostTest.HOST).block();
    });
  }

}
