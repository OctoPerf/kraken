package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.HostService;
import com.kraken.runtime.entity.host.Host;
import com.kraken.runtime.entity.host.HostTest;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {HostController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class HostControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  HostService hostService;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(HostController.class);
  }


  @Test
  public void shouldList() {
    final var hostsFlux = Flux.just(HostTest.HOST);
    given(hostService.list())
        .willReturn(hostsFlux);

    webTestClient.get()
        .uri("/host/list")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Host.class)
        .contains(HostTest.HOST);
  }

  @Test
  public void shouldListAll() {
    final var hostsFlux = Flux.just(HostTest.HOST);
    given(hostService.listAll())
        .willReturn(hostsFlux);

    webTestClient.get()
        .uri("/host/all")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Host.class)
        .contains(HostTest.HOST);
  }

  @Test
  public void shouldAttach() {
    final var host = HostTest.HOST;

    given(hostService.attach(host)).willReturn(Mono.just(host));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/host/attach")
            .build())
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
        .body(BodyInserters.fromValue(host))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Host.class)
        .isEqualTo(host);

    verify(hostService).detach(host);
  }
}