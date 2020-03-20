package com.kraken.storage.file;

import com.kraken.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.Flux;

public interface StorageWatcherService {

  Flux<StorageWatcherEvent> watch();

  Flux<StorageWatcherEvent> watch(String root);
}
