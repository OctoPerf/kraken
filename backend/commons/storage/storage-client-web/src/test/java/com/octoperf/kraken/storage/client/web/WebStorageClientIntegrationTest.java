package com.octoperf.kraken.storage.client.web;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.tests.utils.ResourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

// Start keycloak and make serve-storage before running
@Tag("integration")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class WebStorageClientIntegrationTest {

  @Autowired
  private StorageClientBuilder storageClientBuilder;

  StorageClient storageClient;

  @BeforeEach
  public void setUp() {
    storageClient = storageClientBuilder.build(
        AuthenticatedClientBuildOrder.builder()
            .mode(AuthenticationMode.IMPERSONATE)
            .userId("kraken-user")
            .applicationId("gatling")
            .build()
    ).block();
  }

  @Test
  public void shouldDownloadFile() {
    final var testFile = Paths.get("gatling/README.md");
    final var outFile = Paths.get("testDir/intTest/README.txt");
    StepVerifier.create(storageClient.downloadFile(outFile, testFile.toString()))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldDownloadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/intTest");
    StepVerifier.create(storageClient.downloadFolder(outFile, testFile.toString()))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldUploadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/zipDir");
    StepVerifier.create( storageClient.uploadFile(outFile, testFile.toString()))
        .expectComplete()
        .verify();
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
