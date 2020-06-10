package com.octoperf.kraken.security.user.events.listener;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;


/***
 * Cannot use @RequestParam to fetch form urlencoded values: https://github.com/spring-projects/spring-framework/issues/20738
 */
@Slf4j
@RestController()
@RequestMapping("/user-events")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class UserEventsController {
  @NonNull List<UserEventsService> services;

  @PostMapping("/event/register")
  public Flux<String> register(final ServerWebExchange payload) {
    return payload.getFormData().flatMapMany(data -> {
      final var userId = data.getFirst("user_id");
      final var email = data.getFirst("email");
      final var username = data.getFirst("username");
      log.info(String.format("Register user %s: email: %s, username: %s", userId, email, username));
      return Flux.fromIterable(services).flatMap(service -> service.onRegisterUser(userId, email, username));
    });
  }

  @PostMapping("/event/update_email")
  public Flux<String> updateEmail(final ServerWebExchange payload) {
    return payload.getFormData().flatMapMany(data -> {
      final var userId = data.getFirst("user_id");
      final var updatedEmail = data.getFirst("updated_email");
      final var previousEmail = data.getFirst("previous_email");
      log.info(String.format("Update user %s email: updated: %s, previous: %s", userId, updatedEmail, previousEmail));
      return Flux.fromIterable(services).flatMap(service -> service.onUpdateEmail(userId, updatedEmail, previousEmail));
    });
  }

  @PostMapping("/admin/delete_user")
  public Flux<String> deleteUser(final ServerWebExchange payload) {
    return payload.getFormData().flatMapMany(data -> {
      final var userId = data.getFirst("user_id");
      log.info(String.format("Delete user %s", userId));
      return Flux.fromIterable(services).flatMap(service -> service.onDeleteUser(userId));
    });
  }

  @PostMapping("/admin/create_role")
  public Flux<String> createRole(final ServerWebExchange payload) {
    return payload.getFormData().flatMapMany(data -> {
      final var userId = data.getFirst("user_id");
      final var role = data.getFirst("role");
      log.info(String.format("Create role %s for user %s", role, userId));
      return Flux.fromIterable(services).flatMap(service -> service.onCreateRole(userId, role));
    });
  }

  @PostMapping("/admin/delete_role")
  public Flux<String> deleteRole(final ServerWebExchange payload) {
    return payload.getFormData().flatMapMany(data -> {
      final var userId = data.getFirst("user_id");
      final var role = data.getFirst("role");
      log.info(String.format("Delete role %s for user %s", role, userId));
      return Flux.fromIterable(services).flatMap(service -> service.onDeleteRole(userId, role));
    });
  }

}