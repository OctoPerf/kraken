package com.kraken.analysis.task.events.watcher;

import com.kraken.runtime.event.client.api.RuntimeEventClientBuilder;
import com.kraken.tools.event.bus.EventBus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

import static com.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class TaskEventsWatcher {

  @NonNull RuntimeEventClientBuilder clientBuilder;
  @NonNull EventBus eventBus;

  @PostConstruct
  public void watch() {
    this.clientBuilder.mode(SERVICE_ACCOUNT).build().events()
        .retryBackoff(Integer.MAX_VALUE, Duration.ofSeconds(5))
        .onErrorContinue((throwable, o) -> log.error("Failed to list task events " + o, throwable))
        .subscribe(eventBus::publish);
  }

}
