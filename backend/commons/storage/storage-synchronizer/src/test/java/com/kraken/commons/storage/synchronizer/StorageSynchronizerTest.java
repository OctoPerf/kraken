package com.kraken.commons.storage.synchronizer;

import com.kraken.commons.rest.configuration.ApplicationProperties;
import com.kraken.commons.rest.configuration.ApplicationPropertiesTestConfiguration;
import com.kraken.commons.storage.client.StorageClient;
import com.kraken.commons.storage.synchronizer.properties.StorageSynchronizerProperties;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ApplicationPropertiesTestConfiguration.class})
public class StorageSynchronizerTest {

  @Autowired
  ApplicationProperties applicationProperties;

  @Autowired
  StorageSynchronizerProperties synchronizerProperties;

  @Autowired
  StorageClient storageClient;

  StorageSynchronizer synchronizer;

  @Before
  public void before() {
    synchronizer = new StorageSynchronizer(applicationProperties, synchronizerProperties, storageClient);
  }

  @Test
  public void shouldDownloadAndExtractZip() throws IOException {
    final var testDir = Path.of("testDir");
    Files.createDirectory(testDir);
    final Flux<DataBuffer> flux = DataBufferUtils.read(new FileUrlResource("gatling.zip"),
        new DefaultDataBufferFactory(), DefaultDataBufferFactory.DEFAULT_INITIAL_CAPACITY);

    given(storageClient.getFile(Optional.of(synchronizerProperties.getDownloadFolder()))).willReturn(flux);
    synchronizer.downloadAndExtractZip();
    Assertions.assertThat(Files.list(testDir).count()).isEqualTo(7);
    FileSystemUtils.deleteRecursively(testDir);
  }
}
