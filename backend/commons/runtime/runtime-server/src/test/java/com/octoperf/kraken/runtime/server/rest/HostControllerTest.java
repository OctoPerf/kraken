package com.octoperf.kraken.runtime.server.rest;

import com.octoperf.kraken.runtime.entity.host.Host;
import com.octoperf.kraken.runtime.entity.host.HostTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class HostControllerTest  extends RuntimeControllerTest {

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(HostController.class);
  }


  @Test
  public void shouldList() {
    final var hostsFlux = Flux.just(HostTest.HOST);
    given(hostService.list(userOwner()))
        .willReturn(hostsFlux);

    webTestClient.get()
        .uri("/host/list")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Host.class)
        .contains(HostTest.HOST);
  }

  @Test
  public void shouldListAdmin() {
    webTestClient.get()
        .uri("/host/list")
        .header("Authorization", "Bearer admin-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }


  @Test
  public void shouldListAll() {
    final var hostsFlux = Flux.just(HostTest.HOST);
    given(hostService.listAll())
        .willReturn(hostsFlux);

    webTestClient.get()
        .uri("/host/all")
        .header("Authorization", "Bearer admin-token")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Host.class)
        .contains(HostTest.HOST);
  }

  @Test
  public void shouldListAllForbidden() {
    webTestClient.get()
        .uri("/host/all")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldAttach() {
    final var host = HostTest.HOST;

    given(hostService.attach(host)).willReturn(Mono.just(host));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/host/attach")
            .build())
        .header("Authorization", "Bearer admin-token")
        .body(BodyInserters.fromValue(host))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Host.class)
        .isEqualTo(host);

    verify(hostService).attach(host);
  }

  @Test
  public void shouldDetach() {
    final var host = HostTest.HOST;

    given(hostService.detach(host)).willReturn(Mono.just(host));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/host/detach")
            .build())
        .header("Authorization", "Bearer admin-token")
        .body(BodyInserters.fromValue(host))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Host.class)
        .isEqualTo(host);

    verify(hostService).detach(host);
  }
}