package com.kraken.storage.file;

import com.google.common.collect.ImmutableList;
import com.kraken.Application;
import com.kraken.security.entity.owner.PublicOwner;
import com.kraken.security.entity.owner.UserOwner;
import com.kraken.security.entity.token.KrakenRole;
import com.kraken.storage.entity.StorageNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.ImmutableList.of;
import static com.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FileSystemStorageServiceIntegrationTest {

  @Autowired
  StorageServiceBuilder serviceBuilder;

  StorageService service;

  @MockBean
  FilePart part;

  @Before
  public void before() {
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.write(invocation.getArgument(0), "file content".getBytes(UTF_8));
      return empty();
    });
    service = serviceBuilder.build(PublicOwner.INSTANCE);
  }

  @Test
  public void shouldInit() throws IOException {
    final var path = Path.of("testDir", "users", "user", "gatling", "README.md");
    assertThat(path.toFile().exists()).isFalse();

    serviceBuilder.build(UserOwner.builder().applicationId("gatling")
        .userId("user")
        .roles(ImmutableList.of(KrakenRole.ADMIN))
        .build());
    assertThat(path.toFile().exists()).isFalse();

    serviceBuilder.build(UserOwner.builder().applicationId("gatling")
        .userId("user")
        .roles(ImmutableList.of(KrakenRole.USER))
        .build());
    assertThat(path.toFile().exists()).isTrue();

    FileSystemUtils.deleteRecursively(Path.of("testDir", "users"));
  }

  @Test
  public void shouldList() {
    create(service.list())
        .expectNextCount(25)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldGet() {
    final var filename = "README.md";
    create(service.get(filename))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete().verify();
  }

  @Test
  public void shouldGetContent() {
    create(service.getContent("README.md"))
        .expectNext("Hello!")
        .expectComplete().verify();
  }

  @Test
  public void shouldGetContents() {
    create(service.getContent(ImmutableList.of("visitorTest/dir1/file1.md", "visitorTest/dir1/file2.md")))
        .expectNext("File1")
        .expectNext("File2")
        .expectComplete().verify();
  }

  @Test
  public void shouldSetContentAndDelete() {
    final var filename = "README2.md";
    create(service.setContent(filename, "Some Content"))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete()
        .verify();

    create(service.delete(of(filename)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetContentRenameAndDelete() {
    final var oldName = "oldName.txt";
    final var newName = "newName.txt";
    create(service.setContent(oldName, "Some Content"))
        .expectNextMatches(next -> next.getPath().equals(oldName))
        .expectComplete()
        .verify();

    create(service.rename("", oldName, newName))
        .expectNextMatches(next -> next.getPath().equals(newName))
        .expectComplete()
        .verify();

    create(service.delete(of(newName)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetDirectoryAndDelete() {
    final var path = "some/directory";
    create(service.setDirectory(path))
        .expectNextMatches(next -> next.getPath().equals(path))
        .expectComplete()
        .verify();

    create(service.delete(of("some")))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetRootFileAndDelete() {
    final var filename = "myFile.txt";
    given(part.filename()).willReturn(filename);

    create(service.setFile("", just(part)))
        .expectNextMatches(next -> next.getPath().equals(filename))
        .expectComplete()
        .verify();

    create(service.delete(of(filename)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldSetZip() throws IOException {
    final var filename = "kraken.zip";
    final var destPath = "zipDir/dest";
    given(part.filename()).willReturn(filename);
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.copy(Path.of("testDir/public/zipDir/kraken.zip"), (Path) invocation.getArgument(0));
      return empty();
    });
    create(service.setZip(destPath, Mono.just(part)))
        .expectNextMatches(next -> next.getPath().equals(destPath))
        .expectComplete()
        .verify();

    final var files = service.find(destPath, 1, ".*").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    assertThat(files.size()).isEqualTo(4);
    service.delete(Collections.singletonList(destPath)).blockLast();
  }

  @Test
  public void shouldSetRootFileFailRelativePath() {
    final var filename = "myFile.txt";
    given(part.filename()).willReturn(filename);

    create(service.setFile("../", just(part)))
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  public void shouldSetSubFolderFileAndDelete() {
    final var filename = "myFile.txt";
    final var createdPath = "visitorTest/myFile.txt";
    given(part.filename()).willReturn(filename);

    create(service.setFile("visitorTest", just(part)))
        .expectNextMatches(next -> next.getPath().equals(createdPath) && next.getDepth() == 1)
        .expectComplete()
        .verify();

    create(service.delete(of(createdPath)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldNewSetSubFolderFileAndDelete() throws IOException {
    final var filename = "myFile.txt";
    final var createdPath = "visitorTest/someOther/myFile.txt";
    given(part.filename()).willReturn(filename);

    create(service.setFile("visitorTest/someOther", just(part)))
        .expectNextMatches(next -> next.getPath().equals(createdPath) && next.getDepth() == 2)
        .expectComplete()
        .verify();

    create(service.delete(of(createdPath)))
        .expectNext(true)
        .expectComplete()
        .verify();
    Files.delete(Path.of("testDir/public/visitorTest/someOther"));
  }

  @Test
  public void shouldSetSubFolderFileFailRelativePath() {
    final var filename = "../myFile.txt";
    given(part.filename()).willReturn(filename);

    create(service.setFile("visitorTest", just(part)))
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
    assertThat(filename).isEqualTo("public.zip");
  }

  @Test
  public void shouldGetFileName() {
    final var filename = service.getFileName("README.md");
    assertThat(filename).isEqualTo("README.md");
  }

  @Test
  public void shouldGetDirectoryName() {
    final var filename = service.getFileName("visitorTest");
    assertThat(filename).isEqualTo("visitorTest.zip");
  }

  @Test
  public void shouldMove() {
    final var testFolder = "moveDest";
    create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    create(service.move(ImmutableList.of("moveTest/dir1", "moveTest/dir1/file1.md", "moveTest/dir1/file2.md", "moveTest/dir2/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("moveDest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("moveDest/file1.md"))
        .expectComplete()
        .verify();

    // Revert move
    create(service.move(ImmutableList.of("moveDest/dir1"), "moveTest"))
        .expectNextMatches(next -> next.getPath().equals("moveTest/dir1"))
        .expectComplete()
        .verify();

    create(service.move(ImmutableList.of("moveDest/file1.md"), "moveTest/dir2"))
        .expectNextMatches(next -> next.getPath().equals("moveTest/dir2/file1.md"))
        .expectComplete()
        .verify();

    create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopy() {
    final var testFolder = "copyDest";
    create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    create(service.copy(ImmutableList.of("copyTest/dir1", "copyTest/dir1/file1.md", "copyTest/dir1/file2.md", "copyTest/dir2/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1.md"))
        .expectComplete()
        .verify();

    create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolder() {
    final var testFolder = "copyDest";
    create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    create(service.copy(ImmutableList.of("copyTest/dir1/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1.md"))
        .expectComplete()
        .verify();

    create(service.copy(ImmutableList.of("copyDest/file1.md"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/file1_copy.md"))
        .expectComplete()
        .verify();

    create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolderDot() {
    final var testFolder = "copyDest";
    create(service.setDirectory(testFolder))
        .expectNextMatches(next -> next.getPath().equals(testFolder))
        .expectComplete()
        .verify();

    create(service.copy(ImmutableList.of("copyTest/.someFile"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/.someFile"))
        .expectComplete()
        .verify();

    create(service.copy(ImmutableList.of("copyDest/.someFile"), testFolder))
        .expectNextMatches(next -> next.getPath().equals("copyDest/_copy.someFile"))
        .expectComplete()
        .verify();

    create(service.delete(of(testFolder)))
        .expectNext(true)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindFile() {
    create(service.find("visitorTest/dir1", Integer.MAX_VALUE, "file1\\.md"))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir1/file1.md"))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindSubDirectories() {
    create(service.find("visitorTest/", 1, ".*"))
        .expectNextCount(2)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldFindSubDirectoriesBis() {
    create(service.find("visitorTest/dir1", 1, ".*1\\.md"))
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

    create(service.filterExisting(nodes))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir1"))
        .expectNextMatches(next -> next.getPath().equals("visitorTest/dir2"))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldExtractZip() {
    create(service.extractZip("zipDir/kraken.zip"))
        .expectNextMatches(next -> next.getPath().equals("zipDir/kraken.zip"))
        .expectComplete()
        .verify();
    final var files = service.find("zipDir", 1, "^((?!kraken\\.zip).)*$").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    assertThat(files.size()).isEqualTo(4);
    assertThat(service.delete(files).blockLast()).isTrue();
  }
}
