package com.octoperf.kraken.project.crud.storage;

import com.octoperf.kraken.git.client.api.GitClientBuilder;
import com.octoperf.kraken.project.crud.api.ProjectCrudService;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.project.event.CreateProjectEvent;
import com.octoperf.kraken.project.event.DeleteProjectEvent;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.storage.entity.StorageInitMode;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.time.Instant;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SESSION;
import static com.octoperf.kraken.storage.entity.StorageInitMode.COPY;
import static com.octoperf.kraken.storage.entity.StorageInitMode.EMPTY;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class StorageProjectCrudService implements ProjectCrudService {

  private static final String PROJECT_PATH = Paths.get("project.json").toString();

  @NonNull StorageClientBuilder storageClientBuilder;
  @NonNull EventBus eventBus;
  @NonNull IdGenerator idGenerator;
//  @NonNull GitClientBuilder gitClientBuilder;

  @Override
  public Mono<Project> get(final Owner owner) {
    final var storageClientMono = this.projectStorageClient(owner.getProjectId());
    return storageClientMono.flatMap(storageClient -> storageClient.getJsonContent(PROJECT_PATH, Project.class));
  }

  @Override
  public Flux<Project> list(final Owner owner) {
    final var storageClientMono = this.userStorageClient();
    return storageClientMono.flatMapMany(storageClient ->
        storageClient.find("", 3, PROJECT_PATH)
            .flatMap(node -> storageClient.getJsonContent(node.getPath(), Project.class)));
  }

  @Override
  public Mono<Project> create(final Owner owner, final String applicationId, final String name) {
    return this.createProject(owner, applicationId, name, COPY);
  }

//  @Override
//  public Mono<Project> importFromGit(final Owner owner, final String applicationId, final String name, final String repositoryUrl) {
//    return this.createProject(owner, applicationId, name, EMPTY).flatMap(project -> this.gitClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(SESSION).projectId(project.getId()).applicationId(project.getApplicationId()).build())
//        .flatMap(gitClient -> gitClient.connect(repositoryUrl))
//        .map(empty -> project));
//  }

  @Override
  public Mono<Project> update(final Owner owner, final Project project) {
    final var storageClientMono = this.projectStorageClient(project.getId());
    return storageClientMono.flatMap(storageClient -> storageClient
        .getJsonContent(PROJECT_PATH, Project.class)
        .map(retrieved -> retrieved.toBuilder().name(project.getName()).updateDate(Instant.now().toEpochMilli()).build())
        .flatMap(updated -> storageClient.setJsonContent(PROJECT_PATH, updated).map(storageNode -> updated)));
  }

  private Mono<Project> createProject(final Owner owner, final String applicationId, final String name, final StorageInitMode mode) {
    final var now = Instant.now().toEpochMilli();
    final var project = Project.builder()
        .id(this.idGenerator.generate())
        .name(name)
        .applicationId(applicationId)
        .createDate(now)
        .updateDate(now)
        .build();
    final var initApplication = this.applicationStorageClient(project.getId(), applicationId).flatMap(storageClient -> storageClient.init(mode));
    final var createProjectJson = this.projectStorageClient(project.getId()).flatMap(storageClient -> storageClient.setJsonContent(PROJECT_PATH, project));
    return initApplication.then(createProjectJson)
        .map(storageNode -> project)
        .doOnSubscribe(subscription -> this.eventBus.publish(CreateProjectEvent.builder()
            .owner(owner.toBuilder().applicationId(applicationId).projectId(project.getId()).build())
            .project(project)
            .build())
        );
  }

  @Override
  public Mono<Boolean> delete(final Owner owner, final Project project) {
    final var storageClientMono = this.userStorageClient();
    return storageClientMono.flatMap(storageClient -> storageClient.delete(project.getId()))
        .doOnSubscribe(subscription -> this.eventBus.publish(DeleteProjectEvent.builder()
            .owner(owner.toBuilder().applicationId(project.getApplicationId()).projectId(project.getId()).build())
            .project(project).build()));
  }

  private Mono<StorageClient> userStorageClient() {
    // Returns a storageClient that works on all the user's projects
    return this.storageClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(SESSION).build());
  }

  private Mono<StorageClient> projectStorageClient(final String projectId) {
    // Current storage is the project's root (sibling of the application folder)
    return this.storageClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(SESSION)
        .projectId(projectId)
        .build());
  }

  private Mono<StorageClient> applicationStorageClient(final String projectId, final String applicationId) {
    return this.storageClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(SESSION)
        .projectId(projectId)
        .applicationId(applicationId)
        .build());
  }

}
