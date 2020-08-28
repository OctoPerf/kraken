package com.octoperf.kraken.project.crud.storage;

import com.octoperf.kraken.project.crud.api.ProjectCrudService;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.project.event.CreateProjectEvent;
import com.octoperf.kraken.project.event.DeleteProjectEvent;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class StorageProjectCrudService implements ProjectCrudService {

  private static final String PROJECT_JSON = "project.json";

  @NonNull StorageClientBuilder storageClientBuilder;
  @NonNull EventBus eventBus;
  @NonNull IdGenerator idGenerator;

  @Override
  public Mono<Project> get(final Owner owner) {
    final var storageClientMono = this.currentStorageClient(owner, owner.getProjectId(), owner.getApplicationId());
    return storageClientMono.flatMap(storageClient -> storageClient.getJsonContent(PROJECT_JSON, Project.class));
  }

  @Override
  public Flux<Project> list(final Owner owner) {
    final var storageClientMono = this.projectsStorageClient(owner);
    return storageClientMono.flatMapMany(storageClient ->
        storageClient.find("", 3, PROJECT_JSON)
            .flatMap(node -> storageClient.getJsonContent(node.getPath(), Project.class)));
  }

  @Override
  public Mono<Project> create(final Owner owner, final String applicationId, final String name) {
    final var now = Instant.now().toEpochMilli();
    final var project = Project.builder()
        .id(this.idGenerator.generate())
        .name(name)
        .applicationId(applicationId)
        .createDate(now)
        .updateDate(now)
        .build();
    final var storageClientMono = this.currentStorageClient(owner, project.getId(), applicationId);
    return storageClientMono.flatMap(storageClient -> storageClient.init().then(storageClient.setJsonContent(PROJECT_JSON, project)))
        .map(storageNode -> project)
        .doOnSubscribe(subscription -> this.eventBus.publish(CreateProjectEvent.builder()
            .owner(owner.toBuilder().applicationId(applicationId).projectId(project.getId()).build())
            .project(project)
            .build())
        );
  }

  @Override
  public Mono<Project> update(final Owner owner, final Project project) {
    final var storageClientMono = this.currentStorageClient(owner, project.getId(), project.getApplicationId());
    return storageClientMono.flatMap(storageClient -> storageClient
        .getJsonContent(PROJECT_JSON, Project.class)
        .map(retrieved -> retrieved.toBuilder().name(project.getName()).updateDate(Instant.now().toEpochMilli()).build())
        .flatMap(updated -> storageClient.setJsonContent(PROJECT_JSON, updated).map(storageNode -> updated)));
  }

  @Override
  public Mono<Boolean> delete(final Owner owner, final Project project) {
    final var storageClientMono = this.projectsStorageClient(owner);
    return storageClientMono.flatMap(storageClient -> storageClient.delete(project.getId()))
        .doOnSubscribe(subscription -> this.eventBus.publish(DeleteProjectEvent.builder()
            .owner(owner.toBuilder().applicationId(project.getApplicationId()).projectId(project.getId()).build())
            .project(project).build()));
  }

  private Mono<StorageClient> projectsStorageClient(final Owner owner) {
    // Returns a storageClient that works on all the user's projects
    return this.storageClientBuilder
        .build(AuthenticatedClientBuildOrder.builder().mode(AuthenticationMode.SESSION).userId(owner.getUserId()).build());
  }

  private Mono<StorageClient> currentStorageClient(final Owner owner, final String projectId, final String applicationId) {
    // Returns a storageClient that works on all the user's projects
    return this.storageClientBuilder
        .build(AuthenticatedClientBuildOrder.builder().mode(AuthenticationMode.SESSION)
            .userId(owner.getUserId())
            .projectId(projectId)
            .applicationId(applicationId)
            .build());
  }

}
