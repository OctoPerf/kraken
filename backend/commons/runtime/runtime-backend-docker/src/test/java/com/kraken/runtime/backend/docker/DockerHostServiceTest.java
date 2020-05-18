package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.backend.docker.DockerHostService;
import com.kraken.runtime.entity.host.Host;
import com.kraken.runtime.entity.host.HostCapacity;
import com.kraken.runtime.entity.host.HostTest;
import com.kraken.security.entity.owner.PublicOwner;
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
    final var list = service.list(PublicOwner.INSTANCE).collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0)).isEqualTo(Host.builder()
        .id("local")
        .name("local")
        .capacity(HostCapacity.builder().cpu("-").memory("-").build())
        .allocatable(HostCapacity.builder().cpu("-").memory("-").build())
        .addresses(ImmutableList.of())
        .owner(PublicOwner.INSTANCE)
        .build());
  }

  @Test
  public void shouldListAll() {
    final var list = service.listAll().collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldNotDetach() {
    service.detach(HostTest.HOST).block();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldNotAttach() {
    service.attach(HostTest.HOST).block();
  }

}
