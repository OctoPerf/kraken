package com.octoperf.kraken.project.crud.storage;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.git.client.api.GitClient;
import com.octoperf.kraken.git.client.api.GitClientBuilder;
import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.project.entity.ProjectTest;
import com.octoperf.kraken.project.event.CreateProjectEvent;
import com.octoperf.kraken.project.event.DeleteProjectEvent;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.storage.entity.StorageInitMode;
import com.octoperf.kraken.storage.entity.StorageNodeTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StorageProjectCrudServiceTest {

  @Mock
  StorageClientBuilder storageClientBuilder;
  @Mock
  StorageClient storageClient;
  @Mock
  EventBus eventBus;
  @Mock
  IdGenerator idGenerator;
  @Mock
  GitClientBuilder gitClientBuilder;
  @Mock
  GitClient gitClient;
  @Captor
  ArgumentCaptor<CreateProjectEvent> createProjectEventArgumentCaptor;
  @Captor
  ArgumentCaptor<DeleteProjectEvent> deleteProjectEventArgumentCaptor;
  @Captor
  ArgumentCaptor<Project> projectArgumentCaptor;

  StorageProjectCrudService service;
  Owner owner;

  @BeforeEach
  void before() {
    service = new StorageProjectCrudService(storageClientBuilder, eventBus, idGenerator, gitClientBuilder);
    owner = Owner.builder()
        .userId("userId")
        .roles(ImmutableList.of(KrakenRole.USER))
        .type(OwnerType.USER)
        .build();
  }

  @Test
  void shouldListProjects() {
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .build())).willReturn(Mono.just(storageClient));
    given(storageClient.find("", 3, "project.json")).willReturn(Flux.just(StorageNodeTest.STORAGE_NODE));
    given(storageClient.getJsonContent(StorageNodeTest.STORAGE_NODE.getPath(), Project.class)).willReturn(Mono.just(ProjectTest.PROJECT));
    final var projects = service.list(owner).collectList().block();
    assertThat(projects).isNotNull();
    assertThat(projects.get(0)).isEqualTo(ProjectTest.PROJECT);
  }

  @Test
  void shouldGetProjects() {
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .applicationId(owner.getApplicationId())
        .projectId(owner.getProjectId())
        .build())).willReturn(Mono.just(storageClient));
    given(storageClient.getJsonContent("project.json", Project.class)).willReturn(Mono.just(ProjectTest.PROJECT));
    final var project = service.get(owner).block();
    assertThat(project).isNotNull();
    assertThat(project).isEqualTo(ProjectTest.PROJECT);
  }

  @Test
  void shouldCreateProject() {
    final var appId = "app";
    final var projectId = "projId";
    final var projectName = "projName";
    given(idGenerator.generate()).willReturn(projectId);

    // To write files in the application
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .applicationId(appId)
        .projectId(projectId)
        .build())).willReturn(Mono.just(storageClient));
    // To write files at the project level
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .projectId(projectId)
        .build())).willReturn(Mono.just(storageClient));

    given(storageClient.init(StorageInitMode.COPY)).willReturn(Mono.empty());
    given(storageClient.setJsonContent(eq("project.json"), any(Project.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    final var project = service.create(owner, appId, projectName).block();
    verify(storageClient).init(StorageInitMode.COPY);
    verify(storageClient).setJsonContent(eq("project.json"), projectArgumentCaptor.capture());
    verify(eventBus).publish(createProjectEventArgumentCaptor.capture());

    assertThat(project).isNotNull();
    assertThat(project.getId()).isEqualTo(projectId);
    assertThat(project.getName()).isEqualTo(projectName);
    assertThat(project.getApplicationId()).isEqualTo(appId);

    assertThat(project).isEqualTo(projectArgumentCaptor.getValue());

    final var event = createProjectEventArgumentCaptor.getValue();
    assertThat(event.getProject()).isEqualTo(project);
    assertThat(event.getOwner()).isEqualTo(owner.toBuilder().applicationId(appId).projectId(projectId).build());
  }

  @Test
  void shouldImportProject() {
    final var appId = "app";
    final var projectId = "projId";
    final var projectName = "projName";
    final var repoUrl = "repoUrl";
    given(idGenerator.generate()).willReturn(projectId);

    // To write files in the application
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .applicationId(appId)
        .projectId(projectId)
        .build())).willReturn(Mono.just(storageClient));
    // To write files at the project level
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .projectId(projectId)
        .build())).willReturn(Mono.just(storageClient));

    given(storageClient.init(StorageInitMode.EMPTY)).willReturn(Mono.empty());
    given(storageClient.setJsonContent(eq("project.json"), any(Project.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    given(gitClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .projectId(projectId)
        .applicationId(appId)
        .build())).willReturn(Mono.just(gitClient));
    given(gitClient.connect(repoUrl)).willReturn(Mono.just(GitConfiguration.builder().repositoryUrl(repoUrl).build()));

    final var project = service.importFromGit(owner, appId, projectName, repoUrl).block();
    verify(storageClient).init(StorageInitMode.EMPTY);
    verify(storageClient).setJsonContent(eq("project.json"), projectArgumentCaptor.capture());
    verify(eventBus).publish(createProjectEventArgumentCaptor.capture());

    assertThat(project).isNotNull();
    assertThat(project.getId()).isEqualTo(projectId);
    assertThat(project.getName()).isEqualTo(projectName);
    assertThat(project.getApplicationId()).isEqualTo(appId);

    assertThat(project).isEqualTo(projectArgumentCaptor.getValue());

    final var event = createProjectEventArgumentCaptor.getValue();
    assertThat(event.getProject()).isEqualTo(project);
    assertThat(event.getOwner()).isEqualTo(owner.toBuilder().applicationId(appId).projectId(projectId).build());
  }

  @Test
  void shouldUpdateProject() {
    final var project = ProjectTest.PROJECT;
    final var newName = project.toBuilder().name("newName").build();
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .projectId(project.getId())
        .build())).willReturn(Mono.just(storageClient));
    given(storageClient.getJsonContent("project.json", Project.class)).willReturn(Mono.just(project));
    given(storageClient.setJsonContent(eq("project.json"), any(Project.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    final var result = service.update(owner, newName).block();
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(newName.getName());
    assertThat(result.getUpdateDate()).isGreaterThan(newName.getUpdateDate());

    verify(storageClient).setJsonContent(eq("project.json"), projectArgumentCaptor.capture());
    assertThat(projectArgumentCaptor.getValue()).isEqualTo(result);
  }

  @Test
  void shouldDeleteProject() {
    final var project = ProjectTest.PROJECT;
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.SESSION)
        .build())).willReturn(Mono.just(storageClient));
    given(storageClient.delete(project.getId())).willReturn(Mono.just(true));
    service.delete(owner, project).block();
    verify(eventBus).publish(deleteProjectEventArgumentCaptor.capture());
    final var event = deleteProjectEventArgumentCaptor.getValue();
    assertThat(event.getProject()).isEqualTo(project);
    assertThat(event.getOwner()).isEqualTo(owner.toBuilder().applicationId(project.getApplicationId()).projectId(project.getId()).build());
  }
}
