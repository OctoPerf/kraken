package com.kraken.storage.synchronizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Optional.ofNullable;

@Slf4j
@Configuration
class SynchronizerConfiguration {

  @Autowired
  @Bean
  SynchronizerProperties synchronizerProperties(
      final Function<String, List<FileTransfer>> stringToFileTransfer,
      @Nullable @Value("${kraken.synchronizer.file-downloads:#{environment.KRAKEN_FILE_DOWNLOADS}}") final String fileDownloads,
      @Nullable @Value("${kraken.synchronizer.folder-downloads:#{environment.KRAKEN_FOLDER_DOWNLOADS}}") final String folderDownloads,
      @Nullable @Value("${kraken.synchronizer.file-uploads:#{environment.KRAKEN_FILE_UPLOADS}}") final String fileUploads
  ) {

    final var properties = SynchronizerProperties.builder()
        .fileDownloads(ofNullable(fileDownloads).map(stringToFileTransfer).orElse(of()))
        .folderDownloads(ofNullable(folderDownloads).map(stringToFileTransfer).orElse(of()))
        .fileUploads(ofNullable(fileUploads).map(stringToFileTransfer).orElse(of()))
        .build();

    log.info(properties.toString());
    return properties;
  }


}

