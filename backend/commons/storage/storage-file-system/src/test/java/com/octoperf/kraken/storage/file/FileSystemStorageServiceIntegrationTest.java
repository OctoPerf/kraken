package com.octoperf.kraken.storage.file;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.storage.entity.StorageInitMode;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.codec.multipart.FilePart;

import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.ImmutableList.of;
import static com.octoperf.kraken.storage.entity.StorageNodeType.*;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class FileSystemStorageServiceIntegrationTest {

  @Autowired
  StorageServiceBuilder serviceBuilder;

  StorageService service;

  @MockBean
  FilePart part;

  List<StorageWatcherEvent> events;

  @BeforeEach
  public void before() {
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.write(invocation.getArgument(0), "file content".getBytes(UTF_8));
      return empty();
    });
    service = serviceBuilder.build(Owner.PUBLIC);

    events = new LinkedList<>();
    service.watch("").subscribe(storageWatcherEvent -> events.add(storageWatcherEvent));
  }

  @AfterEach
  public void after() {
    events.forEach(System.out::println);
  }

  @Test
  public void shouldInitFail() {
    final var serviceAdmin = serviceBuilder.build(Owner.builder()
        .applicationId("gatling")
        .projectId("project-id")
        .userId("user")
        .roles(ImmutableList.of(KrakenRole.ADMIN))
        .type(OwnerType.USER)
        .build());
    create(serviceAdmin.init(StorageInitMode.COPY))
        .expectError()
        .verify();
  }

  @Test
  public void shouldInit() throws IOException {
    final var path = Path.of("testDir", "users", "user", "project-id", "gatling", "README.md");
    assertThat(path.toFile().exists()).isFalse();
    final var serviceUser =serviceBuilder.build(Owner.builder().applicationId("gatling")
        .userId("user")
        .projectId("project-id")
        .roles(ImmutableList.of(KrakenRole.USER))
        .type(OwnerType.USER)
        .build());
    create(serviceUser.init(StorageInitMode.COPY))
        .expectComplete()
        .verify();
    assertThat(path.toFile().exists()).isTrue();
    FileSystemUtils.deleteRecursively(Path.of("testDir", "users"));
  }

  @Test
  public void shouldInitEmpty() throws IOException {
    final var path = Path.of("testDir", "users", "user", "project-id", "gatling");
    assertThat(path.toFile().exists()).isFalse();
    final var serviceUser =serviceBuilder.build(Owner.builder().applicationId("gatling")
        .userId("user")
        .projectId("project-id")
        .roles(ImmutableList.of(KrakenRole.USER))
        .type(OwnerType.USER)
        .build());
    create(serviceUser.init(StorageInitMode.EMPTY))
        .expectComplete()
        .verify();
    assertThat(path.toFile().exists()).isTrue();
    assertThat(path.resolve("README.md").toFile().exists()).isFalse();
    FileSystemUtils.deleteRecursively(Path.of("testDir", "users"));
  }

  @Test
  public void shouldList() {
    create(service.list())
        .expectNextCount(33)
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

    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("README2.md").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("README2.md").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var filename = "README2.md";

    checkResult(service.setContent(filename, "Some Content"), events.subList(0, 1));
    checkResult(service.delete(of(filename)), events.subList(1, 2));
    checkEvents(events);
  }

  @Test
  public void shouldSetContentSubFolderAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content").type(FILE).depth(0).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content/README2.md").type(FILE).depth(1).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content/README2.md").type(FILE).depth(1).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content").type(FILE).depth(0).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var filename = "content/README2.md";

    checkResult(service.setContent(filename, "Some Content"), events.subList(0, 2));
    checkResult(service.delete(of("content")), events.subList(2, 4));
    checkEvents(events);
  }

  @Test
  public void shouldSetContentTwiceSubFolderAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content").type(FILE).depth(0).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content/README2.md").type(FILE).depth(1).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content/README2.md").type(FILE).depth(1).length(13L).lastModified(0L).build()).type(MODIFY).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content/README2.md").type(FILE).depth(1).length(13L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("content").type(FILE).depth(0).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var filename = "content/README2.md";
    checkResult(service.setContent(filename, "Some Content"), events.subList(0, 2));
    checkResult(service.setContent(filename, "Other Content"), events.subList(2, 3));
    checkResult(service.delete(of("content")), events.subList(3, 5));
    checkEvents(events);
  }

  @Test
  public void shouldSetContentTwiceAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("README2.md").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("README2.md").type(FILE).depth(0).length(13L).lastModified(0L).build()).type(MODIFY).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("README2.md").type(FILE).depth(0).length(13L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var filename = "README2.md";
    checkResult(service.setContent(filename, "Some Content"), events.subList(0, 1));
    checkResult(service.setContent(filename, "Other Content"), events.subList(1, 2));
    checkResult(service.delete(of(filename)), events.subList(2, 3));
    checkEvents(events);
  }

  @Test
  public void shouldSetContentRenameAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("oldName.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("oldName.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("newName.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("newName.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var oldName = "oldName.txt";
    final var newName = "newName.txt";
    checkResult(service.setContent(oldName, "Some Content"), events.subList(0, 1));
    checkResult(service.rename("", oldName, newName), events.subList(1, 3));
    checkResult(service.delete(of(newName)), events.subList(3, 4));
    checkEvents(events);
  }

  @Test
  public void shouldSetDirectoryAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("some").type(DIRECTORY).depth(0).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("some/directory").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("some/directory").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("some").type(DIRECTORY).depth(0).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var path = "some/directory";
    checkResult(service.setDirectory(path), events.subList(0, 2));
    checkResult(service.delete(of("some")), events.subList(2, 4));
    checkEvents(events);
  }

  @Test
  public void shouldSetRootFileAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("myFile.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("myFile.txt").type(FILE).depth(0).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var filename = "myFile.txt";
    given(part.filename()).willReturn(filename);

    checkResult(service.setFile("", just(part)), events.subList(0, 1));
    checkResult(service.delete(of(filename)), events.subList(1, 2));
    checkEvents(events);
  }

  @Test
  public void shouldSetZip() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken-dev-architecture.png").type(FILE).depth(2).length(63356L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken-preview.gif").type(FILE).depth(2).length(2283806L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/README.md").type(FILE).depth(2).length(3138L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/version.txt").type(FILE).depth(2).length(6L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken-dev-architecture.png").type(FILE).depth(2).length(63356L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/version.txt").type(FILE).depth(2).length(6L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/README.md").type(FILE).depth(2).length(3138L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken-preview.gif").type(FILE).depth(2).length(2283806L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest").type(FILE).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var filename = "kraken.zip";
    final var destPath = "zipDir/dest";
    given(part.filename()).willReturn(filename);
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.copy(Path.of("testDir/public/zipDir/kraken.zip"), (Path) invocation.getArgument(0));
      return empty();
    });
    checkResult(service.setZip(destPath, Mono.just(part)), events.subList(0, 5));

    final var files = service.find(destPath, 1, ".*").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    assertThat(files.size()).isEqualTo(4);
    service.delete(Collections.singletonList(destPath)).blockLast();

    checkEvents(events);
  }

  @Test
  public void shouldSetZipWithSubFolders() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest").type(DIRECTORY).depth(1).length(4096L).lastModified(1596199830512L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken").type(DIRECTORY).depth(2).length(4096L).lastModified(1596199830536L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/README.md").type(FILE).depth(3).length(3138L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/version").type(DIRECTORY).depth(3).length(4096L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/version/version.txt").type(FILE).depth(4).length(6L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images").type(DIRECTORY).depth(3).length(4096L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/kraken-preview.gif").type(FILE).depth(4).length(2283806L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/arch").type(DIRECTORY).depth(4).length(4096L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/arch/kraken-dev-architecture.png").type(FILE).depth(5).length(63356L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/arch/kraken-dev-architecture.png").type(FILE).depth(5).length(63356L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/arch").type(DIRECTORY).depth(4).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images/kraken-preview.gif").type(FILE).depth(4).length(2283806L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/images").type(DIRECTORY).depth(3).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/README.md").type(FILE).depth(3).length(3138L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/version/version.txt").type(FILE).depth(4).length(6L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken/version").type(DIRECTORY).depth(3).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest/kraken").type(DIRECTORY).depth(2).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/dest").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var filename = "kraken-sub.zip";
    final var destPath = "zipDir/dest";
    given(part.filename()).willReturn(filename);
    given(part.transferTo(any(Path.class))).will(invocation -> {
      Files.copy(Path.of("testDir/public/zipDir/kraken-sub.zip"), (Path) invocation.getArgument(0));
      return empty();
    });
    checkResult(service.setZip(destPath, Mono.just(part)), events.subList(0, 9));

    final var files = service.find(destPath, 4, ".*").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    assertThat(files.size()).isEqualTo(8);

    service.delete(Collections.singletonList(destPath)).blockLast();

    checkEvents(events);
  }

  @Test
  public void shouldExtractZip() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken").type(DIRECTORY).depth(1).length(4096L).lastModified(1596199830536L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/README.md").type(FILE).depth(2).length(3138L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/version").type(DIRECTORY).depth(2).length(4096L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/version/version.txt").type(FILE).depth(3).length(6L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images").type(DIRECTORY).depth(2).length(4096L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/kraken-preview.gif").type(FILE).depth(3).length(2283806L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/arch").type(DIRECTORY).depth(3).length(4096L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/arch/kraken-dev-architecture.png").type(FILE).depth(4).length(63356L).lastModified(1596199830552L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/arch/kraken-dev-architecture.png").type(FILE).depth(4).length(63356L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/arch").type(DIRECTORY).depth(3).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images/kraken-preview.gif").type(FILE).depth(3).length(2283806L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/images").type(DIRECTORY).depth(2).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/README.md").type(FILE).depth(2).length(3138L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/version/version.txt").type(FILE).depth(3).length(6L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken/version").type(DIRECTORY).depth(2).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/kraken").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var path = "zipDir/kraken-sub.zip";

    checkResult(service.extractZip(path), events.subList(0, 8));

    final var files = service.find("zipDir/kraken", 4, ".*").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    assertThat(files.size()).isEqualTo(7);

    create(service.delete(Collections.singletonList("zipDir/kraken")))
        .expectNextCount(8)
        .expectComplete()
        .verify();

    checkEvents(events);
  }

  @Test
  public void shouldExtractGitZip() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/_git").type(DIRECTORY).depth(1).length(4096L).lastModified(1596199830536L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/_git/config").type(FILE).depth(2).length(287L).lastModified(1596199830540L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/_git/config").type(FILE).depth(2).length(287L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("zipDir/_git").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var path = "zipDir/git.zip";

    checkResult(service.extractZip(path), events.subList(0, 2));

    final var files = service.find("zipDir/_git", 1, ".*").map(StorageNode::getPath).collect(Collectors.toList()).block();
    assertThat(files).isNotNull();
    System.out.println(files);
    assertThat(files.size()).isEqualTo(1);

    create(service.delete(Collections.singletonList("zipDir/_git")))
        .expectNextCount(2)
        .expectComplete()
        .verify();

    checkEvents(events);

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
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/myFile.txt").type(FILE).depth(1).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/myFile.txt").type(FILE).depth(1).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var filename = "myFile.txt";
    final var createdPath = "visitorTest/myFile.txt";
    given(part.filename()).willReturn(filename);

    checkResult(service.setFile("visitorTest", just(part)), events.subList(0, 1));
    checkResult(service.delete(of(createdPath)), events.subList(1, 2));
    checkEvents(events);
  }

  @Test
  public void shouldNewSetSubFolderFileAndDelete() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/someOther").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/someOther/myFile.txt").type(FILE).depth(2).length(12L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/someOther/myFile.txt").type(FILE).depth(2).length(12L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("visitorTest/someOther").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );
    final var filename = "myFile.txt";
    final var createdPath = "visitorTest/someOther/myFile.txt";
    given(part.filename()).willReturn(filename);

    checkResult(service.setFile("visitorTest/someOther", just(part)), events.subList(0, 2));
    checkResult(service.delete(of("visitorTest/someOther")), events.subList(2, 4));
    checkEvents(events);
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
    service.getFileInputStream("").subscribe(inputStream -> {
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
    service.getFileInputStream("visitorTest/dir1/file1.md").subscribe(inputStream -> {
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
  public void shouldGetFileResource() {
    final var resource = service.getFileResource("visitorTest/dir1/file1.md").block();
    assertThat(resource).isNotNull();
    assertThat(resource.exists()).isTrue();
    assertThat(resource.getFilename()).isEqualTo("file1.md");
  }

  @Test
  public void shouldGetFolderResource() {
    final var resource = service.getFileResource("").block();
    assertThat(resource).isNotNull();
    assertThat(resource.exists()).isTrue();
    assertThat(resource.getFilename()).endsWith(".zip");
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
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest").type(DIRECTORY).depth(0).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/dir1").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/dir1/file1.md").type(FILE).depth(2).length(9L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/dir1/file2.md").type(FILE).depth(2).length(9L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/file1.md").type(FILE).depth(1).length(9L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveTest/dir1/file1.md").type(FILE).depth(2).length(9L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveTest/dir1/file2.md").type(FILE).depth(2).length(9L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveTest/dir1").type(DIRECTORY).depth(1).length(4096L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveTest/dir2/file1.md").type(FILE).depth(2).length(9L).lastModified(0L).build()).type(DELETE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "moveDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.move(ImmutableList.of("moveTest/dir1", "moveTest/dir1/file1.md", "moveTest/dir1/file2.md", "moveTest/dir2/file1.md"), testFolder), events.subList(1, 9));

    checkEvents(events);

    // Revert move
    create(service.move(ImmutableList.of("moveDest/dir1"), "moveTest"))
        .expectNextCount(6)
        .expectComplete()
        .verify();

    create(service.move(ImmutableList.of("moveDest/file1.md"), "moveTest/dir2"))
        .expectNextCount(2)
        .expectComplete()
        .verify();

    create(service.delete(of(testFolder)))
        .expectNextCount(1)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldMoveSameFolder() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest").type(DIRECTORY).depth(0).length(4096L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/file1.md").type(FILE).depth(1).length(9L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("moveDest/file1_copy.md").type(FILE).depth(1).length(9L).lastModified(0L).build()).type(CREATE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "moveDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("moveTest/dir1/file1.md"), testFolder), events.subList(1, 2));
    checkResult(service.copy(ImmutableList.of("moveDest/file1.md"), testFolder), events.subList(2, 3));
    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(3)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopy() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest").type(DIRECTORY).depth(0).length(4096L).lastModified(1596464461633L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1").type(DIRECTORY).depth(1).length(4096L).lastModified(1596464465849L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1/dir11").type(DIRECTORY).depth(2).length(4096L).lastModified(1596464466261L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1/dir11/file12.md").type(FILE).depth(3).length(10L).lastModified(1596464466865L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1/dir11/file11.md").type(FILE).depth(3).length(10L).lastModified(1596464467089L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1/file1.md").type(FILE).depth(2).length(9L).lastModified(1596464467261L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/dir1/file2.md").type(FILE).depth(2).length(9L).lastModified(1596464467473L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1.md").type(FILE).depth(1).length(9L).lastModified(1596464467933L).build()).type(MODIFY).owner(Owner.PUBLIC).build()
    );
    final var testFolder = "copyDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("copyTest/dir1", "copyTest/dir1/file1.md", "copyTest/dir1/file2.md", "copyTest/dir2/file1.md", "copyTest/dir3/file1.md"), testFolder), events.subList(1, 9));
    checkEvents(events);

    final var content = service.getContent("copyDest/file1.md").block();
    assertThat(content).isEqualTo("dir3File1");

    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(8)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolder() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest").type(DIRECTORY).depth(0).length(4096L).lastModified(1596464461633L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1_copy.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "copyDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("copyTest/dir1/file1.md"), testFolder), events.subList(1, 2));
    checkResult(service.copy(ImmutableList.of("copyDest/file1.md"), testFolder), events.subList(2, 3));
    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(3)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolder2() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest").type(DIRECTORY).depth(0).length(4096L).lastModified(1596464461633L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file2.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1_copy.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file2_copy.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "copyDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("copyTest/dir1/file1.md", "copyTest/dir1/file2.md"), testFolder), events.subList(1, 3));
    checkResult(service.copy(ImmutableList.of("copyDest/file1.md", "copyDest/file2.md"), testFolder), events.subList(3, 5));
    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(5)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolder3() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest").type(DIRECTORY).depth(0).length(4096L).lastModified(1596464461633L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/file1_copy.md").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "copyDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("copyTest/dir1/file1.md"), testFolder), events.subList(1, 2));
    checkResult(service.copy(ImmutableList.of("copyDest/file1.md"), testFolder), events.subList(2, 3));
    create(service.copy(ImmutableList.of("copyDest/file1.md"), testFolder)).expectComplete();
    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(3)
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldCopySameFolderDot() {
    final var events = ImmutableList.of(
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest").type(DIRECTORY).depth(0).length(4096L).lastModified(1596464461633L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/.someFile").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build(),
        StorageWatcherEvent.builder().node(StorageNode.builder().path("copyDest/_copy.someFile").type(FILE).depth(1).length(9L).lastModified(1596464467713L).build()).type(CREATE).owner(Owner.PUBLIC).build()
    );

    final var testFolder = "copyDest";
    checkResult(service.setDirectory(testFolder), events.subList(0, 1));
    checkResult(service.copy(ImmutableList.of("copyTest/.someFile"), testFolder), events.subList(1, 2));
    checkResult(service.copy(ImmutableList.of("copyDest/.someFile"), testFolder), events.subList(2, 3));
    checkEvents(events);

    create(service.delete(of(testFolder)))
        .expectNextCount(3)
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

  private void checkResult(final Flux<StorageWatcherEvent> flux, final List<StorageWatcherEvent> expectedEvents) {
    final var result = flux.collectList().block();
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(expectedEvents.size());
    for (int i = 0; i < result.size(); i++) {
      final var current = result.get(i);
      final var expected = expectedEvents.get(i);
      assertThat(current.getNode().getPath()).isEqualTo(expected.getNode().getPath());
      assertThat(current.getNode().getDepth()).isEqualTo(expected.getNode().getDepth());
      assertThat(current.getNode().getLength()).isEqualTo(expected.getNode().getLength());
      assertThat(current.getType()).isEqualTo(expected.getType());
      assertThat(current.getOwner()).isEqualTo(expected.getOwner());
    }
  }

  private void checkEvents(final List<StorageWatcherEvent> expectedEvents) {
    this.checkResult(Flux.fromIterable(events), expectedEvents);
  }
}
