package com.kraken.influxdb.client.web;

import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.tools.unique.id.IdGenerator;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
final class WebInfluxDBClient implements InfluxDBClient {

  IdGenerator idGenerator;
  WebClient webClient;

  WebInfluxDBClient(@NonNull final InfluxDBProperties properties,
                    @NonNull final IdGenerator idGenerator) {
    super();
    this.idGenerator = idGenerator;
    this.webClient = WebClient
        .builder()
        .baseUrl(properties.getUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuthorizationHeader(properties.getUser(), properties.getPassword()))
        .build();
  }

  @Override
  public Mono<String> deleteSeries(final String database, final String testId) {
    return retry(webClient.post()
        .uri(uri -> uri.path("/query").queryParam("db", database).build())
        .body(fromFormData("q", format("DROP SERIES FROM /.*/ WHERE test = '%s'", testId)))
        .retrieve()
        .bodyToMono(String.class), log);
  }

  @Override
  public Mono<InfluxDBUser> createUser(final String userId) {
    final String id = sanitize(userId);

    final var user = InfluxDBUser.builder()
        .username(id)
        .database(id)
        .password(idGenerator.generate())
        .build();

    final var createUser = retry(webClient.post()
        .uri(uri -> uri.path("/query").build())
        .body(fromFormData("q", format("CREATE USER %s WITH PASSWORD '%s'", user.getUsername(), user.getPassword())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var createDB = retry(webClient.post()
        .uri(uri -> uri.path("/query").build())
        .body(fromFormData("q", format("CREATE DATABASE %s", user.getDatabase())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var grantPrivileges = retry(webClient.post()
        .uri(uri -> uri.path("/query").build())
        .body(fromFormData("q", format("GRANT ALL ON %s TO %s", user.getDatabase(), user.getUsername())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return createUser.then(createDB).then(grantPrivileges).map(s -> user);
  }

  @Override
  public Mono<String> deleteUser(final String userId) {
    final String id = sanitize(userId);

    final var dropDB = retry(webClient.post()
        .uri(uri -> uri.path("/query").build())
        .body(fromFormData("q", format("DROP DATABASE %s", id)))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var dropUser = retry(webClient.post()
        .uri(uri -> uri.path("/query").build())
        .body(fromFormData("q", format("DROP USER %s", id)))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return dropDB.then(dropUser).map(s -> userId);
  }

  public String sanitize(final String userId) {
    return "kraken_" + userId.replaceAll("-", "_");
  }
}
