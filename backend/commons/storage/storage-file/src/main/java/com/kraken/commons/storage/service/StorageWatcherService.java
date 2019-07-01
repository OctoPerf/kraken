package com.kraken.commons.storage.service;

import com.kraken.commons.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.Flux;

public interface StorageWatcherService {

  Flux<StorageWatcherEvent> watch();
}
