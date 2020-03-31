package com.kraken.security.configuration;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController()
@RequestMapping("/test")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class TestController {

  @NonNull AuthenticatedUserProvider userProvider;

  @GetMapping("/admin")
  public Mono<String> admin() {
    return Mono.just("hello admin");
  }

  @GetMapping("/user")
  public Mono<String> user() {
    return userProvider.getAuthenticatedUser().map(user -> "hello " + user.getUsername());
  }
}
