package com.kraken.storage.synchronizer;

import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ApplicationPropertiesTestConfiguration.class, StringToFileTransfer.class})
public class StringToFileTransferTest {

  @Autowired
  Function<String, FileTransfer> stringToFileTransfer;

  @Test
  public void shouldConvert() {
    assertThat(stringToFileTransfer.apply("local<->remote")).isEqualTo(FileTransfer.builder()
        .localPath(Path.of("testDir/local"))
        .remotePath("remote")
        .build());
  }
}
