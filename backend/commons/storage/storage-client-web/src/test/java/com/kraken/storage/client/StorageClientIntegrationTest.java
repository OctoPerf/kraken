package com.kraken.storage.client;

import com.kraken.analysis.entity.Result;
import com.kraken.test.utils.ResourceUtils;
import com.kraken.test.utils.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("make serve-storage before running")
@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
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

    client.downloadFolder(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldUploadFolder() {
    final var testFile = Paths.get("gatling/");
    final var outFile = Paths.get("testDir/zipDir");

    client.uploadFile(outFile, testFile.toString()).block();
  }

  @Test
  public void shouldDownloadJsonAndYaml() throws IOException {
    final var jsonPath = "result.json.kraken";
    final var yamlPath = "result.yaml.kraken";
    client.setContent(jsonPath, ResourceUtils.getResourceContent(jsonPath)).block();
    client.setContent(yamlPath, ResourceUtils.getResourceContent(jsonPath)).block();
    final var resultJson = client.getJsonContent(jsonPath, Result.class).block();
    final var resultYaml = client.getYamlContent(yamlPath, Result.class).block();
    assertThat(resultJson).isEqualTo(resultYaml);
    client.delete(jsonPath).block();
    client.delete(yamlPath).block();
  }

  @Test
  public void shouldDownloadContent() throws IOException {
    final var path = "test.txt";
    final var content = ResourceUtils.getResourceContent(path);
    client.setContent(path, content).block();
    final var result = client.getContent(path).block();
    assertThat(result).isEqualTo(content);
    client.delete(path).block();
  }
}
