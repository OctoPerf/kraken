package com.kraken.commons.storage.file;

import com.google.common.collect.ImmutableList;
import com.kraken.commons.rest.configuration.ApplicationPropertiesTestConfiguration;
import com.kraken.commons.storage.TestConfiguration;
import com.kraken.commons.storage.entity.StorageNode;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.ImmutableList.of;
import static com.kraken.commons.storage.entity.StorageNodeType.DIRECTORY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationPropertiesTestConfiguration.class, TestConfiguration.class})
public class FileSystemStorageServiceIntegrationTest {

  @Autowired
  StorageService service;

  @MockBean
  FilePart part;

  @Before
  public void before() {
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.write(invocation.getArgument(0), "file content".getBytes(UTF_8));
      return empty();
    });
  }

  @Test
  public void shouldList() {
    StepVerifier.create(service.list())
        .expectNextCount(23)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldGet() {
    final var filename = "README.md";
    StepVerifier.create(service.get(filename))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete().verify();
  }

  @Test
  public void shouldGetContent() {
    StepVerifier.create(service.getContent("README.md"))
        .expectNext("Hello!")
        .expectComplete().verify();
  }

  @Test
  public void shouldGetContents() {
    StepVerifier.create(service.getContent(ImmutableList.of("visitorTest/dir1/file1.md", "visitorTest/dir1/file2.md")))
        .expectNext("File1")
        .expectNext("File2")
        .expectComplete().verify();
  }

  @Test
  public void shouldSetContentAndDelete() {
    final var filename = "README2.md";
    StepVerifier.create(service.setContent(filename, "Some Content"))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(filename)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetContentRenameAndDelete() {
    final var oldName = "oldName.txt";
    final var newName = "newName.txt";
    StepVerifier.create(service.setContent(oldName, "Some Content"))
        .expectNextMatches(next -> next.getPath().equals(oldName))
        .expectComplete()
        .verify();

    StepVerifier.create(service.rename("", oldName, newName))
        .expectNextMatches(next -> next.getPath().equals(newName))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(newName)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetDirectoryAndDelete() {
    final var path = "some/directory";
    StepVerifier.create(service.setDirectory(path))
        .expectNextMatches(next -> next.getPath().equals(path))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of("some")))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetRootFileAndDelete() {
    final var filename = "myFile.txt";
    given(part.filename()).willReturn(filename);

    StepVerifier.create(service.setFile("", just(part)))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(filename)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetRootFileFailRelativePath() {
    final var filename = "myFile.txt";
    given(part.filename()).willReturn(filename);

    StepVerifier.create(service.setFile("../", just(part)))
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  public void shouldSetSubFolderFileAndDelete() {
    final var filename = "myFile.txt";
    final var createdPath = "visitorTest/myFile.txt";
    given(part.filename()).willReturn(filename);

    StepVerifier.create(service.setFile("visitorTest", just(part)))
        .expectNextMatches(next -> next.getPath().equals(createdPath) && next.getDepth() == 1)
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(createdPath)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetSubFolderFileFailRelativePath() {
    final var filename = "../myFile.txt";
    given(part.filename()).willReturn(filename);

    StepVerifier.create(service.setFile("visitorTest", just(part)))
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  public void shouldGetFolder() {
    final var filename = "getFile.zip";
    service.getFile("").subscribe(inputStream -> {
      try {
        Files.copy(inputStream, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        Assert.fail(e.getMessage());
      }
    });

    final var file = Paths.get(filename).toFile();
    Assert.assertTrue(file.exists());
    file.deleteOnExit();
  }

  @Test
  public void shouldGetFile() {
    final var filename = "getFile.md";
    service.getFile("visitorTest/dir1/file1.md").subscribe(inputStream -> {
      try {
        Files.copy(inputStream, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        Assert.fail(e.getMessage());
      }
    });

    final var file = Paths.get(filename).toFile();
    Assert.assertTrue(file.exists());
    file.deleteOnExit();
  }

  @Test
  public void shouldGetRootFileName() {
    final var filename = service.getFileName("");
    Assertions.assertThat(filename).isEqualTo("testDir.zip");
  }

  @Test
  public void shouldGetFileName() {
    final var filename = service.getFileName("README.md");
    Assertions.assertThat(filename).isEqualTo("README.md");
  }

  @Test
  public void shouldGetDirectoryName() {
    final var filename = service.getFileName("visitorTest");
    Assertions.assertThat(filename).isEqualTo("visitorTest.zip");
  }

  @Test
  public void shouldMove() {
    final var testFolder = "moveDest";
    StepVerifier.create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    StepVerifier.create(service.move(ImmutableList.of("moveTest/dir1", "moveTest/dir1/file1.md", "moveTest/dir1/file2.md", "moveTest/dir2/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("moveDest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("moveDest/file1.md"))
        .expectComplete()
        .verify();

    // Revert move
    StepVerifier.create(service.move(ImmutableList.of("moveDest/dir1"), "moveTest"))
        .expectNextMatches(next -> next.getPath().equals("moveTest/dir1"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.move(ImmutableList.of("moveDest/file1.md"), "moveTest/dir2"))
        .expectNextMatches(next -> next.getPath().equals("moveTest/dir2/file1.md"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopy() {
    final var testFolder = "copyDest";
    StepVerifier.create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    StepVerifier.create(service.copy(ImmutableList.of("copyTest/dir1", "copyTest/dir1/file1.md", "copyTest/dir1/file2.md", "copyTest/dir2/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1.md"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolder() {
    final var testFolder = "copyDest";
    StepVerifier.create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    StepVerifier.create(service.copy(ImmutableList.of("copyTest/dir1/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1.md"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.copy(ImmutableList.of("copyDest/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1_copy.md"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolderDot() {
    final var testFolder = "copyDest";
    StepVerifier.create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    StepVerifier.create(service.copy(ImmutableList.of("copyTest/.someFile"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/.someFile"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.copy(ImmutableList.of("copyDest/.someFile"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/_copy.someFile"))
        .expectComplete()
        .verify();

    StepVerifier.create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindFile() {
    StepVerifier.create(service.find("visitorTest/dir1", Integer.MAX_VALUE, "file1\\.md"))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir1/file1.md"))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindSubDirectories() {
    StepVerifier.create(service.find("visitorTest/", 1, ".*"))
        .expectNextCount(2)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindSubDirectoriesBis() {
    StepVerifier.create(service.find("visitorTest/dir1", 1, ".*1\\.md"))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir1/file1.md"))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFilterExisting() {
    final var nodes = ImmutableList.of(
        StorageNode.builder()
            .path("visitorTest/dir1")
            .type(DIRECTORY)
            .depth(1)
            .lastModified(0L)
            .length(0L)
            .build(),
        StorageNode.builder()
            .path("visitorTest/dir2")
            .type(DIRECTORY)
            .depth(1)
            .lastModified(0L)
            .length(0L)
            .build(),
        StorageNode.builder()
            .path("visitorTest/dir3")
            .type(DIRECTORY)
            .depth(1)
            .lastModified(0L)
            .length(0L)
            .build()
    );

    StepVerifier.create(service.filterExisting(nodes))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir2"))
        .expectComplete()
        .verify();
  }
}
