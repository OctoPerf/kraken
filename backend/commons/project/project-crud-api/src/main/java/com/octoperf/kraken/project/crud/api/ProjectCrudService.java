package com.octoperf.kraken.project.crud.api;

import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectCrudService {

  Mono<Project> get(Owner owner);

  Flux<Project> list(Owner owner);

  Mono<Project> create(Owner owner, String applicationId, String name);

  Mono<Project> update(Owner owner, Project project);

  Mono<Boolean> delete(Owner owner, Project project);

}
