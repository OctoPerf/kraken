package com.kraken.commons.docker.client.spotify;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.executor.CommandExecutor;
import com.kraken.commons.docker.client.entity.DockerContainer;
import com.kraken.commons.docker.client.entity.DockerImage;
import com.kraken.test.utils.TestUtils;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerSpotifyServiceTest {

  @Mock
  CommandExecutor executor;
  @Mock
  DockerClient client;
  @Mock
  Function<String, ContainerConfig> ymlToConfig;

  private Mono<String> commandId;
  private DockerSpotifyService service;
  private ContainerConfig configuration = ContainerConfig.builder().image("image").build();

  @Before
  public void before() {
    commandId = Mono.just("commandId");
    given(executor.execute(any())).willReturn(commandId);
    given(ymlToConfig.apply(anyString())).willReturn(configuration);
    service = new DockerSpotifyService(client, executor, ymlToConfig);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(DockerSpotifyService.class);
  }

  @Test
  public void shouldPull() {
    assertThat(service.pull("app", "image")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker", "pull", "image"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldPs() throws DockerException, InterruptedException {
    final Container container = mock(Container.class);
    given(container.id()).willReturn("id");
    given(container.names()).willReturn(ImmutableList.of("name1", "name2"));
    given(container.image()).willReturn("image");
    given(container.status()).willReturn("status");
    given(client.listContainers(allContainers())).willReturn(ImmutableList.of(container));
    final var result = service.ps();
    StepVerifier.create(result).expectNext(DockerContainer.builder()
        .id("id")
        .name("name1, name2")
        .image("image")
        .status("status")
        .full(container)
        .build())
        .expectComplete().verify();
    verify(client).listContainers(allContainers());
  }

  @Test
  public void shouldPsFail() throws DockerException, InterruptedException {
    final var exception = new InterruptedException();
    given(client.listContainers(allContainers())).willThrow(exception);
    final var result = service.ps();
    StepVerifier.create(result).expectError(InterruptedException.class).verify();
    verify(client).listContainers(allContainers());
  }

  @Test
  public void shouldRun() throws DockerException, InterruptedException {
    final ContainerCreation containerCreation = mock(ContainerCreation.class);
    given(containerCreation.id()).willReturn("id");
    given(client.createContainer(any(), any())).willReturn(containerCreation);
    final Mono<String> result = service.run("name", "config");
    StepVerifier.create(result).expectNext("id")
        .expectComplete()
        .verify();
    verify(client).createContainer(any(), any());
  }

  @Test
  public void shouldLogs() {
    assertThat(service.logs("app", "containerId")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker", "logs", "-f", "containerId"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldStart() throws DockerException, InterruptedException {
    final Mono<Boolean> result = service.start("containerId");
    StepVerifier.create(result).expectNext(true)
        .expectComplete()
        .verify();
    verify(client).startContainer("containerId");
  }

  @Test
  public void shouldStop() throws DockerException, InterruptedException {
    final Mono<Boolean> result = service.stop("containerId", 10);
    StepVerifier.create(result).expectNext(true)
        .expectComplete()
        .verify();
    verify(client).stopContainer("containerId", 10);
  }

  @Test
  public void shouldTail() throws DockerException, InterruptedException {
    final var logs = mock(LogStream.class);
    given(logs.readFully()).willReturn("logs");
    given(client.logs(any(), any())).willReturn(logs);
    final Mono<String> result = service.tail("containerId", 3);
    StepVerifier.create(result).expectNext("logs")
        .expectComplete()
        .verify();
    verify(client).logs("containerId", DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr(), DockerClient.LogsParam.tail(3));
  }


  @Test
  public void shouldInspect() throws DockerException, InterruptedException {
    final var state = mock(ContainerState.class);
    given(state.status()).willReturn("status");
    final var info = mock(ContainerInfo.class);
    given(info.id()).willReturn("id");
    given(info.name()).willReturn("name");
    given(info.image()).willReturn("image");
    given(info.state()).willReturn(state);

    given(client.inspectContainer("containerId")).willReturn(info);
    final Mono<DockerContainer> result = service.inspect("containerId");
    StepVerifier.create(result).expectNext(DockerContainer.builder()
        .id("id").name("name").image("image").status("status").full(info).build())
        .expectComplete()
        .verify();
    verify(client).inspectContainer("containerId");
  }

  @Test
  public void shouldRm() throws DockerException, InterruptedException {
    final Mono<Boolean> result = service.rm("containerId");
    StepVerifier.create(result).expectNext(true)
        .expectComplete()
        .verify();
    verify(client).removeContainer("containerId", DockerClient.RemoveContainerParam.forceKill(), DockerClient.RemoveContainerParam.removeVolumes());
  }

  @Test
  public void shouldImages() throws DockerException, InterruptedException {
    final Image image1 = mock(Image.class);
    given(image1.id()).willReturn("id1");
    given(image1.created()).willReturn("created1");
    given(image1.size()).willReturn(42L);
    given(image1.repoDigests()).willReturn(ImmutableList.of("repoDigestStart@repoDigestEnd"));
    given(image1.repoTags()).willReturn(ImmutableList.of("repoTagStart:repoTagEnd"));

    final Image image2 = mock(Image.class);
    given(image2.id()).willReturn("id2");
    given(image2.created()).willReturn("created2");
    given(image2.size()).willReturn(1337L);
    given(image2.repoDigests()).willReturn(null);
    given(image2.repoTags()).willReturn(null);

    given(client.listImages()).willReturn(ImmutableList.of(image1, image2));

    final var result = service.images();
    StepVerifier.create(result)
        .expectNext(
            DockerImage.builder()
                .id("id1")
                .name("repoDigestStart")
                .tag("repoTagEnd")
                .created("created1")
                .size(42L)
                .full(image1)
                .build()
        )
        .expectNext(
            DockerImage.builder()
                .id("id2")
                .name("<none>")
                .tag("<none>")
                .created("created2")
                .size(1337L)
                .full(image2)
                .build()
        )
        .expectComplete().verify();
    verify(client).listImages();
  }

  @Test
  public void shouldImagesFail() throws DockerException, InterruptedException {
    final var exception = new InterruptedException();
    given(client.listImages()).willThrow(exception);
    final var result = service.images();
    StepVerifier.create(result).expectError(InterruptedException.class).verify();
    verify(client).listImages();
  }

  @Test
  public void shouldRmi() throws DockerException, InterruptedException {
    final Mono<Boolean> result = service.rmi("imageId");
    StepVerifier.create(result).expectNext(true)
        .expectComplete()
        .verify();
    verify(client).removeImage("imageId", true, false);
  }

  @Test
  public void shouldPrune() {
    assertThat(service.prune("app", false, false)).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker", "system", "prune", "-f"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldPruneAllVolumes() {
    assertThat(service.prune("app", true, true)).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker", "system", "prune", "-f", "--all", "--volumes"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build());
  }
}
