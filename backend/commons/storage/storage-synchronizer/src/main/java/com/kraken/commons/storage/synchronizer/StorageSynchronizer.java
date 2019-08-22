package com.kraken.commons.storage.synchronizer;

import com.google.common.annotations.VisibleForTesting;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import com.kraken.commons.storage.client.StorageClient;
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

  @PostConstruct
  public void init() throws IOException {
    downloadAndExtractZip();
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
}
