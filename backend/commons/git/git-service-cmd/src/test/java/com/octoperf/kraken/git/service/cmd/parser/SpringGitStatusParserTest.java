package com.octoperf.kraken.git.service.cmd.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.tests.utils.ResourceUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class SpringGitStatusParserTest {

  @MockBean
  ApplicationProperties properties;
  @MockBean
  UserProvider userProvider;

  @Autowired
  ObjectMapper mapper;
  @Autowired
  SpringGitStatusParser parser;

  @Test
  void shouldParseStatus0() throws Exception {
    this.shouldParseStatus("0");
  }

  @Test
  void shouldParseStatus1() throws Exception {
    this.shouldParseStatus("1");
  }

  @Test
  void shouldParseStatus2() throws Exception {
    this.shouldParseStatus("2");
  }

  @Test
  void shouldParseStatus3() throws Exception {
    this.shouldParseStatus("3");
  }

  @Test
  void shouldParseStatus4() throws Exception {
    this.shouldParseStatus("4");
  }

  @Test
  void shouldParseStatus5() throws Exception {
    this.shouldParseStatus("5");
  }

  private void shouldParseStatus(final String suffix) throws Exception {
    final var status = ResourceUtils.getResourceContent("git-status-" + suffix + ".txt");
    final var lines = status.split("\\r?\\n");
    final var parsed = parser.apply(Flux.fromArray(lines)).block();
    Assertions.assertThat(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed))
        .isEqualTo(ResourceUtils.getResourceContent("git-status-" + suffix + ".json"));
  }

}