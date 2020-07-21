package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaAdminClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUser;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.octoperf.kraken.tools.webclient.Client.basicAuthorizationHeader;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
final class WebGrafanaAdminClient implements GrafanaAdminClient {

  WebClient webClient;
  IdGenerator idGenerator;
  ObjectMapper mapper;

  WebGrafanaAdminClient(@NonNull final GrafanaProperties properties,
                        @NonNull final IdGenerator idGenerator,
                        @NonNull final ObjectMapper mapper) {
    this.webClient = WebClient
        .builder()
        .baseUrl(properties.getUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuthorizationHeader(properties.getUser(), properties.getPassword()))
        .build();
    this.idGenerator = idGenerator;
    this.mapper = mapper;
  }

  @Override
  public Mono<GrafanaUser> createUser(final String userId, final String email) {
    final var password = idGenerator.generate();

    final var createUser = retry(this.webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/api/admin/users").build())
        .body(BodyInserters.fromValue(CreateGrafanaUserRequest.builder()
            .name(email)
            .email(email)
            .login(userId)
            .password(password)
            .build()))
        .retrieve()
        .bodyToMono(CreateGrafanaUserResponse.class), log);

    // GET http://localhost:3000/grafana/api/users/3/orgs
    // [{"orgId":1,"name":"Main Org.","role":"Viewer"}]
    final Function<CreateGrafanaUserResponse, Mono<GrafanaUser>> withOrgId = (CreateGrafanaUserResponse response) -> retry(this.webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/users/{id}/orgs").build(response.getId()))
        .retrieve()
        .bodyToMono(String.class), log)
        .flatMap(orgResponse -> Mono.fromCallable(() -> {
          final ArrayNode responseNode = (ArrayNode) mapper.readTree(orgResponse);
          return GrafanaUser.builder()
              .id(response.getId().toString())
              .email(email)
              .password(password)
              .datasourceName(userId)
              .orgId(responseNode.get(0).get("orgId").asText())
              .build();
        }));

    return createUser.flatMap(withOrgId)
        .doOnSubscribe(subscription -> log.info(String.format("Create Grafana user %s %s", userId, email)))
        .doOnError(throwable -> log.error("Failed to create Grafana user", throwable));
  }


  @Override
  public Mono<String> deleteUser(final String userId) {

    final var findUser = retry(webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/users/lookup")
            .queryParam("loginOrEmail", userId)
            .build(userId))
        .retrieve()
        .bodyToMono(FindGrafanaUserResponse.class), log);

    final Function<FindGrafanaUserResponse, Mono<String>> deleteUser = (FindGrafanaUserResponse response) -> retry(webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/api/admin/users/{id}").build(response.getId()))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final Function<FindGrafanaUserResponse, Mono<String>> deleteOrganization = (FindGrafanaUserResponse response) -> retry(webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/api/orgs/{id}").build(response.getOrgId()))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return findUser
        .flatMap(response -> Mono.zip(deleteUser.apply(response), deleteOrganization.apply(response)))
        .map(t2 -> userId)
        .doOnSubscribe(subscription -> log.info(String.format("Delete Grafana user %s", userId)))
        .doOnError(throwable -> log.error("Failed to delete Grafana user", throwable));
  }

  @Override
  public Mono<GrafanaUser> updateUser(final GrafanaUser user, final String userId, final String email) {
    final var updateUser = retry(this.webClient.put()
        .uri(uriBuilder -> uriBuilder.path("/api/users/{id}").build(user.getId()))
        .body(BodyInserters.fromValue(UpdateGrafanaUserRequest.builder()
            .name(email)
            .email(email)
            .login(userId)
            .build()))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var updateOrganization = retry(this.webClient.put()
        .uri(uriBuilder -> uriBuilder.path("/api/orgs/{id}").build(user.getOrgId()))
        .body(BodyInserters.fromValue(UpdateGrafanaOrganizationRequest.builder()
            .name(email)
            .build()))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return updateUser.then(updateOrganization).map(response ->
        user.toBuilder()
            .email(email)
            .build())
        .doOnSubscribe(subscription -> log.info(String.format("Update Grafana user %s %s", userId, email)))
        .doOnError(throwable -> log.error("Failed to update Grafana user", throwable));

  }
}
