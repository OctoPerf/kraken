package com.kraken.runtime.client;

import com.kraken.Application;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RuntimeClientConfigurationTest {

  @Qualifier("webClientRuntime")
  @Autowired
  WebClient runtimeWebClient;

  @Test
  public void shouldCreateWebClients() {
    Assertions.assertThat(runtimeWebClient).isNotNull();
  }
}
