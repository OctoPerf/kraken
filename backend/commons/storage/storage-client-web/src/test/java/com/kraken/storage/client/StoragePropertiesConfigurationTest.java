package com.kraken.storage.client;

import com.kraken.config.storage.api.StorageProperties;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JacksonConfiguration.class, StorageWebClient.class})
public class StoragePropertiesConfigurationTest {
  @Autowired
  StorageClient client;
  @MockBean
  StorageProperties properties;

  @Test
  public void shouldCreateWebClients() {
    assertThat(properties).isNotNull();
    assertThat(client).isNotNull();
  }
}
