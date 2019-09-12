package com.kraken.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;
import java.util.Optional;

@Ignore("make serve-storage before running")
@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {StorageWebClient.class, StorageClientConfiguration.class, ObjectMapper.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class StorageClientIntegrationTest {

  @Autowired
  private StorageClient client;

  @Test
  public void shouldDownloadFile() {
    final var testFile = Paths.get("gatling/README.md");
    final var outFile = Paths.get("testDir/intTest/README.txt");

    client.downloadFile(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldDownloadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/intTest");

    client.downloadFolder(outFile, Optional.of(testFile.toString())).block();
  }

  @Test
  public void shouldUploadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/zipDir");

    client.uploadFile(outFile, Optional.of(testFile.toString())).block();
  }
}
