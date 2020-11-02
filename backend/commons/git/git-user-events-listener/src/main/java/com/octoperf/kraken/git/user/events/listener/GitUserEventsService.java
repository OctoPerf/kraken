package com.octoperf.kraken.git.user.events.listener;

import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.security.user.events.listener.UserEventsServiceAdapter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class GitUserEventsService extends UserEventsServiceAdapter {

  @NonNull GitUserService userService;

  @Override
  public Mono<String> onRegisterUser(final String userId, final String email, final String username) {
    return this.userService.initCredentials(userId).map(gitCredentials -> userId);
  }
}
