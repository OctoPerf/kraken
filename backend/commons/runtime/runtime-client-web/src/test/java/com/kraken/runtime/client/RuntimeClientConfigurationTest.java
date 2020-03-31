package com.kraken.runtime.client;

import com.kraken.Application;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RuntimeClientConfigurationTest {
  @Autowired
  RuntimeClient client;
  @MockBean
  RuntimeClientProperties properties;

  @Test
  public void shouldCreateWebClients() {
    assertThat(client).isNotNull();
  }
}
