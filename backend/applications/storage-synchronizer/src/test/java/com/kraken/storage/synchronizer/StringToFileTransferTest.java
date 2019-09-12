package com.kraken.storage.synchronizer;

import com.google.common.collect.ImmutableList;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ApplicationPropertiesTestConfiguration.class, StringToFileTransfer.class})
public class StringToFileTransferTest {

  @Autowired
  Function<String, List<FileTransfer>> stringToFileTransfer;

  @Test
  public void shouldConvert() {
    assertThat(stringToFileTransfer.apply("local<->remote, local1<->remote1, <->")).isEqualTo(ImmutableList.of(FileTransfer.builder()
            .localPath(Path.of("testDir/local"))
            .remotePath("remote")
            .build(),
        FileTransfer.builder()
            .localPath(Path.of("testDir/local1"))
            .remotePath("remote1")
            .build(),
        FileTransfer.builder()
            .localPath(Path.of("testDir"))
            .remotePath("")
            .build()));
  }
}
