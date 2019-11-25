package com.kraken.gatling.log.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ParserIntegrationTest {

  @Autowired
  LogParser parser;

  @Test
  public void shouldHandleGatlingFile() {
    final var path = Paths.get("testDir/gatling.log");
    final var entries = parser.parse(path).take(Duration.ofSeconds(3)).collectList().block();
    assertThat(entries).isNotNull();
    assertThat(entries.size()).isEqualTo(13);
  }
}
