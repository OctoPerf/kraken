package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.HostService;
import com.kraken.runtime.entity.Host;
import com.kraken.runtime.entity.HostTest;
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
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

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

}