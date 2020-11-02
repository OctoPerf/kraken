package com.octoperf.kraken.runtime.logs;

import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.log.LogStatus;
import com.octoperf.kraken.runtime.entity.log.LogType;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.log.AbstractLogService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringTaskLogsService extends AbstractLogService<Log> implements TaskLogsService {


  ConcurrentMap<String, Disposable> subscriptions = new ConcurrentHashMap<>();

  @Override
  public boolean dispose(final Owner owner, final String id, final LogType type) {
    if (this.subscriptions.containsKey(id)) {
      this.add(Log.builder().owner(owner).id(id).type(type).text("").status(LogStatus.CLOSED).build());
      final var subscription = this.subscriptions.get(id);
      subscription.dispose();
      return true;
    }
    return false;
  }

  @Override
  public Disposable push(final Owner owner, final String id, final LogType type, final Flux<String> stringFlux) {
    final var subscription = super.concat(stringFlux)
        .map(text -> Log.builder().owner(owner).id(id).type(type).text(text).status(LogStatus.RUNNING).build())
        .concatWith(Flux.just(Log.builder().owner(owner).id(id).type(type).text("").status(LogStatus.CLOSED).build()))
        .doOnTerminate(() -> subscriptions.remove(id))
        .subscribeOn(Schedulers.elastic())
        .subscribe(this::add);
    this.subscriptions.put(id, subscription);
    return subscription;
  }

  @Override
  public void clear() {
    super.clear();
    subscriptions.values().forEach(Disposable::dispose);
    subscriptions.clear();
  }
}
