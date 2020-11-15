package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.git.entity.GitLog;
import com.octoperf.kraken.git.service.api.GitLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.log.AbstractLogService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringGitLogsService extends AbstractLogService<GitLog> implements GitLogsService {

  private static final Duration TIMEOUT = Duration.ofMinutes(5);

  @Override
  public void add(Owner owner, String text) {
    this.add(GitLog.builder().owner(owner).text(text).build());
  }


  @Override
  public Disposable push(final Owner owner, final Flux<String> stringFlux) {
    return super.concat(stringFlux)
        .subscribeOn(Schedulers.elastic())
        .timeout(TIMEOUT)
        .subscribe(text -> this.add(owner, text));
  }
}
