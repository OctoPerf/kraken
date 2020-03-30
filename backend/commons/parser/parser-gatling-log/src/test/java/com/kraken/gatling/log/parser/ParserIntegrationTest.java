package com.kraken.gatling.log.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

  @Test
  public void shouldHandleGatlingOverTime() throws IOException {
    final var test = Paths.get("testDir/test.log");
    final var debug = Paths.get("testDir/debug.log");
    Files.deleteIfExists(test);
    Files.createFile(test);
    new Thread(() -> {
      try {
        final var lines = Files.readAllLines(debug);
        Thread.sleep(100);
        for (int i = 0; i < lines.size(); i++) {
          Files.writeString(test, lines.get(i) + "\r\n", StandardOpenOption.APPEND);
          if (Math.floorMod(i, 100) == 0){
            Thread.sleep(50);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();

    final var entries = parser.parse(test).take(Duration.ofSeconds(5)).collectList().block();
    assertThat(entries).isNotNull();
    assertThat(entries.size()).isEqualTo(13);
  }

  @Test
  public void shouldHandleGatlingOverTimeNew() throws IOException {
    final var test = Paths.get("testDir/testNew.log");
    final var debug = Paths.get("testDir/debug.log");
    Files.deleteIfExists(test);
    Files.createFile(test);
    new Thread(() -> {
      try {
        final var lines = Files.readAllLines(debug);
        Thread.sleep(100);
        for (int i = 0; i < lines.size(); i++) {
          final var text = String.join("\r\n", lines.subList(0, i + 1));
          Files.writeString(test, text , StandardOpenOption.CREATE);
          if (Math.floorMod(i, 100) == 0){
            Thread.sleep(50);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();

    final var entries = parser.parse(test).take(Duration.ofSeconds(6)).collectList().block();
    assertThat(entries).isNotNull();
    assertThat(entries.size()).isEqualTo(13);
  }
}
