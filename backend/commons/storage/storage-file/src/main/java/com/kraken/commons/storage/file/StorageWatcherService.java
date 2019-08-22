package com.kraken.commons.storage.file;

import com.kraken.commons.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.Flux;

public interface StorageWatcherService {

  Flux<StorageWatcherEvent> watch();
}
