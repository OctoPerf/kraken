package com.kraken.storage.synchronizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
class SynchronizerConfiguration {

  @Autowired
  @Bean
  SynchronizerProperties synchronizerProperties(
      final Function<String, FileTransfer> stringToFileTransfer,
      @Value("#{'${kraken.synchronizer.file-downloads:#{environment.KRAKEN_FILE_DOWNLOADS}}'.split(',')}") final List<String> fileDownloads,
      @Value("#{'${kraken.synchronizer.folder-downloads:#{environment.KRAKEN_FOLDER_DOWNLOADS}}'.split(',')}") final List<String> folderDownloads,
      @Value("#{'${kraken.synchronizer.file-uploads:#{environment.KRAKEN_FILE_UPLOADS}}'.split(',')}") final List<String> fileUploads,
      @Value("#{'${kraken.synchronizer.folder-uploads:#{environment.KRAKEN_FOLDER_UPLOADS}}'.split(',')}") final List<String> folderUploads
  ) {

    final var properties = SynchronizerProperties.builder()
        .fileDownloads(fileDownloads.stream().map(stringToFileTransfer).collect(Collectors.toList()))
        .folderDownloads(folderDownloads.stream().map(stringToFileTransfer).collect(Collectors.toList()))
        .fileUploads(fileUploads.stream().map(stringToFileTransfer).collect(Collectors.toList()))
        .folderUploads(folderUploads.stream().map(stringToFileTransfer).collect(Collectors.toList()))
        .build();

    log.info(properties.toString());
    return properties;
  }


}

