package com.octoperf.kraken.influxdb.client.web;

import com.octoperf.kraken.influxdb.client.api.InfluxDBClient;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor
final class WebInfluxDBClient implements InfluxDBClient {

  private static final String QUERY = "/query";
  
  IdGenerator idGenerator;
  WebClient webClient;

  @Override
  public Mono<String> deleteSeries(final String database, final String testId) {
    return retry(webClient.post()
        .uri(uri -> uri.path(QUERY).queryParam("db", database).build())
        .body(fromFormData("q", format("DROP SERIES FROM /.*/ WHERE test = '%s'", testId)))
        .retrieve()
        .bodyToMono(String.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Delete InfluxDB series %s %s", database, testId)))
        .doOnError(throwable -> log.error("Failed to delete InfluxDB series", throwable));
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
        .uri(uri -> uri.path(QUERY).build())
        .body(fromFormData("q", format("CREATE USER %s WITH PASSWORD '%s'", user.getUsername(), user.getPassword())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var createDB = retry(webClient.post()
        .uri(uri -> uri.path(QUERY).build())
        .body(fromFormData("q", format("CREATE DATABASE %s", user.getDatabase())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var grantPrivileges = retry(webClient.post()
        .uri(uri -> uri.path(QUERY).build())
        .body(fromFormData("q", format("GRANT ALL ON %s TO %s", user.getDatabase(), user.getUsername())))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return createUser.then(createDB).then(grantPrivileges).map(s -> user)
        .doOnSubscribe(subscription -> log.info(String.format("Create InfluxDB user %s", userId)))
        .doOnError(throwable -> log.error("Failed to create InfluxDB user", throwable));
  }

  @Override
  public Mono<String> deleteUser(final String userId) {
    final String id = sanitize(userId);

    final var dropDB = retry(webClient.post()
        .uri(uri -> uri.path(QUERY).build())
        .body(fromFormData("q", format("DROP DATABASE %s", id)))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    final var dropUser = retry(webClient.post()
        .uri(uri -> uri.path(QUERY).build())
        .body(fromFormData("q", format("DROP USER %s", id)))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return dropDB.then(dropUser).map(s -> userId)
        .doOnSubscribe(subscription -> log.info(String.format("Delete InfluxDB user %s", userId)))
        .doOnError(throwable -> log.error("Failed to delete InfluxDB user", throwable));
  }

  public String sanitize(final String userId) {
    return "kraken_" + userId.replaceAll("-", "_");
  }
}
