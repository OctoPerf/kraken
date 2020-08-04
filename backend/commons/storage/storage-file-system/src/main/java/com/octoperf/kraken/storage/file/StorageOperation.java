package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.FluxSink;

@FunctionalInterface
public interface StorageOperation {
  void call(FluxSink<StorageWatcherEvent> sink) throws Exception;
}
