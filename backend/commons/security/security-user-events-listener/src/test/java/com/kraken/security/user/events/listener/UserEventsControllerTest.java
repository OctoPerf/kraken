package com.kraken.security.user.events.listener;

import com.kraken.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEventsControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  UserEventsService service;

  @Test
  public void shouldRegister() {
    final var userId = "userId";
    final var email = "email";
    final var username = "username";
    given(service.onRegisterUser(userId, email, username)).willReturn(Mono.just(userId));
    webTestClient.post()
        .uri("/user-events/event/register")
        .body(BodyInserters.fromFormData("user_id", userId)
            .with("email", email)
            .with("username", username))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo(userId);
  }

  @Test
  public void updateEmail() {
    final var userId = "userId";
    final var updatedEmail = "updated_email";
    final var previousEmail = "previous_email";
    given(service.onUpdateEmail(userId, updatedEmail, previousEmail)).willReturn(Mono.just(userId));
    webTestClient.post()
        .uri("/user-events/event/update_email")
        .body(BodyInserters.fromFormData("user_id", userId)
            .with("updated_email", updatedEmail)
            .with("previous_email", previousEmail))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo(userId);
  }

  @Test
  public void deleteUser() {
    final var userId = "userId";
    given(service.onDeleteUser(userId)).willReturn(Mono.just(userId));
    webTestClient.post()
        .uri("/user-events/admin/delete_user")
        .body(BodyInserters.fromFormData("user_id", userId))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo(userId);
  }

  @Test
  public void createRole() {
    final var userId = "userId";
    final var role = "role";
    given(service.onCreateRole(userId, role)).willReturn(Mono.just(userId));
    webTestClient.post()
        .uri("/user-events/admin/create_role")
        .body(BodyInserters.fromFormData("user_id", userId)
            .with("role", role))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo(userId);
  }

  @Test
  public void deleteRole() {
    final var userId = "userId";
    final var role = "role";
    given(service.onDeleteRole(userId, role)).willReturn(Mono.just(userId));
    webTestClient.post()
        .uri("/user-events/admin/delete_role")
        .body(BodyInserters.fromFormData("user_id", userId)
            .with("role", role))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo(userId);
  }
}