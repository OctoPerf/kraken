package com.octoperf.kraken.git.user.events.listener;

import com.octoperf.kraken.git.entity.GitCredentialsTest;
import com.octoperf.kraken.git.service.api.GitUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GitUserEventsServiceTest {

  @Mock
  GitUserService userService;

  GitUserEventsService service;

  @BeforeEach
  void setUp() {
    service = new GitUserEventsService(userService);
  }

  @Test
  void shouldOnRegisterUser() {
    final var userId = "userId";
    BDDMockito.given(userService.initCredentials(userId)).willReturn(Mono.just(GitCredentialsTest.GIT_CREDENTIALS));
    Assertions.assertThat(service.onRegisterUser(userId, "", "").block()).isNotNull().isEqualTo(userId);
    BDDMockito.verify(userService).initCredentials(userId);
  }
}