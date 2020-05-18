package com.kraken.storage.client.web;

import com.kraken.Application;
import com.kraken.analysis.entity.Result;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import com.kraken.tests.utils.ResourceUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("start keycloak and make serve-storage before running")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class WebStorageClientIntegrationTest {

  @Autowired
  private StorageClientBuilder storageClientBuilder;

  StorageClient storageClient;

  @Before
  public void setUp(){
    storageClient = storageClientBuilder.applicationId("gatling").mode(AuthenticationMode.IMPERSONATE, "kraken-user").build();
  }

  @Test
  public void shouldDownloadFile() {
    final var testFile = Paths.get("gatling/README.md");
    final var outFile = Paths.get("testDir/intTest/README.txt");

    storageClient.downloadFile(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldDownloadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/intTest");

    storageClient.downloadFolder(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldUploadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/zipDir");

    storageClient.uploadFile(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldDownloadJsonAndYaml() throws IOException {
    final var jsonPath = "result.json.kraken";
    final var yamlPath = "result.yaml.kraken";
    storageClient.setContent(jsonPath, ResourceUtils.getResourceContent(jsonPath)).block();
    storageClient.setContent(yamlPath, ResourceUtils.getResourceContent(jsonPath)).block();
    final var resultJson = storageClient.getJsonContent(jsonPath, Result.class).block();
    final var resultYaml = storageClient.getYamlContent(yamlPath, Result.class).block();
    assertThat(resultJson).isEqualTo(resultYaml);
    storageClient.delete(jsonPath).block();
    storageClient.delete(yamlPath).block();
  }

  @Test
  public void shouldDownloadContent() throws IOException {
    final var path = "test.txt";
    final var content = ResourceUtils.getResourceContent(path);
    storageClient.setContent(path, content).block();
    final var result = storageClient.getContent(path).block();
    assertThat(result).isEqualTo(content);
    storageClient.delete(path).block();
  }
}
