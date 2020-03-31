package com.kraken.analysis.client;

import com.kraken.Application;
import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AnalysisClientConfigTest {
  @Autowired
  AnalysisClient client;
  @MockBean
  AnalysisClientProperties properties;

  @Test
  public void shouldCreateWebClients() {
    when(properties.getUrl()).thenReturn("http://localhost");
    assertThat(client).isNotNull();
  }
}
