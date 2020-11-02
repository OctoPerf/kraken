package com.octoperf.kraken.project.server.rest;

import com.octoperf.kraken.project.crud.api.ProjectCrudService;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController()
@RequestMapping("/project")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ProjectController {

  @NonNull ProjectCrudService projectCrudService;
  @NonNull UserProvider userProvider;

  @PostMapping(value = "/delete", produces = TEXT_PLAIN_VALUE)
  public Mono<String> delete(@RequestBody() final Project project) {
    log.info(String.format("Delete project %s", project.getId()));
    return this.userProvider.getOwner("", "")
        .flatMap(owner -> projectCrudService.delete(owner, project))
        .map(Objects::toString);
  }

  @PostMapping()
  public Mono<Project> create(@RequestParam("applicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                              @RequestParam("name") @Size(min = 3, max = 64) final String name) {
    log.info(String.format("Create project %s", name));
    return this.userProvider.getOwner("", "")
        .flatMap(owner -> projectCrudService.create(owner, applicationId, name));
  }

  @PostMapping("/import")
  public Mono<Project> importFromGit(@RequestParam("applicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                     @RequestParam("name") @Size(min = 3, max = 64) final String name,
                                     @RequestParam("repositoryUrl") @Pattern(regexp = "git@[^\\:]+[\\:][^\\/:]+\\/.+\\.git") final String repositoryUrl) {
    log.info(String.format("Create project %s", name));
    return this.userProvider.getOwner("", "")
        .flatMap(owner -> projectCrudService.importFromGit(owner, applicationId, name, repositoryUrl));
  }

  @PutMapping()
  public Mono<Project> update(@RequestBody() final Project project) {
    log.info(String.format("Update project %s", project.getId()));
    return this.userProvider.getOwner("", "")
        .flatMap(owner -> projectCrudService.update(owner, project));
  }

  @GetMapping()
  public Mono<Project> get(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                           @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    log.info("Get project");
    return userProvider.getOwner(applicationId, projectId).flatMap(this.projectCrudService::get);
  }

  @GetMapping(value = "/list")
  public Flux<Project> list() {
    log.info("List projects");
    return userProvider.getOwner("", "")
        .flatMapMany(this.projectCrudService::list);
  }
}
