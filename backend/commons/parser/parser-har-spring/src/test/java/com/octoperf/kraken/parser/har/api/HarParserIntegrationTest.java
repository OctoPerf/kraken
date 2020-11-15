package com.octoperf.kraken.parser.har.api;

import com.octoperf.kraken.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class HarParserIntegrationTest {

  @Autowired
  HarParser parser;

  @Test
  public void shouldHandleHarFile() {
    final var list = parser.parse(Paths.get("testDir/app.octoperf.com.har")).collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(13);
    // Check the stripe request (2nd in the HAR, first in order)
    final var first = list.get(1);
    System.out.println(first);
    assertThat(first.getId()).isEqualTo("request_0");
    assertThat(first.getRequestUrl()).isEqualTo("POST https://m.stripe.com/4");
  }

  @Test
  public void shouldHandleHttpBinHarFile() {
    final var list = parser.parse(Paths.get("testDir/httpbin.har")).collectList().block();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(5);
  }
}
