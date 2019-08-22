package com.kraken.commons.storage.synchronizer;

import com.google.common.annotations.VisibleForTesting;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import com.kraken.commons.storage.client.StorageClient;
import com.kraken.commons.storage.entity.StorageWatcherEvent;
import com.kraken.commons.storage.file.StorageWatcherService;
import com.kraken.commons.storage.synchronizer.properties.StorageSynchronizerProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class StorageSynchronizer {

  @NonNull
  ApplicationProperties applicationProperties;

  @NonNull
  StorageSynchronizerProperties synchronizerProperties;

  @NonNull StorageClient storageClient;

  @NonNull StorageWatcherService watcherService;

  @PostConstruct
  public void init() throws IOException {
    this.downloadAndExtractZip();
    this.reportUpdates();
  }

  @VisibleForTesting
  public void downloadAndExtractZip() throws IOException {
    final var out = applicationProperties.getData().resolve("storage.zip");
    final Flux<DataBuffer> flux = storageClient.getFile(Optional.of(synchronizerProperties.getDownloadFolder()));
    DataBufferUtils.write(flux, new FileOutputStream(out.toFile()).getChannel())
        .map(DataBufferUtils::release).blockLast();
    ZipUtil.unpack(out.toFile(), applicationProperties.getData().toFile());
    Files.delete(out);
  }

  @VisibleForTesting
  public void reportUpdates() {
    watcherService.watch()
        .filter(storageWatcherEvent -> storageWatcherEvent.getNode().getPath().matches(synchronizerProperties.getUpdateFilter()))
//        TODO Regroup events by path and take only the last one every N seconds
        .subscribe(this::handleWatcherEvent);
  }

  @VisibleForTesting
  public void handleWatcherEvent(final StorageWatcherEvent event) {
    switch (event.getEvent()){
      case "MODIFY":
      case "CREATE":
//        storageClient.
//        TODO filtrer les créations/modifications de directory
        break;
      case "DELETE":
//        TODO ignorer les suppressions ? y'en a d'ailleurs ?
        break;

    }
  }
}
