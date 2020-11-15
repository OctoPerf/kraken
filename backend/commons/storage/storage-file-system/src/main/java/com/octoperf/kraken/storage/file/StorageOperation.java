package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.FluxSink;

import java.io.IOException;

@FunctionalInterface
public interface StorageOperation {
  void call(FluxSink<StorageWatcherEvent> sink) throws IOException;
}
