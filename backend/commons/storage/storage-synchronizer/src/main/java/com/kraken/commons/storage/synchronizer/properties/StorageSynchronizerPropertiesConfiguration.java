package com.kraken.commons.storage.synchronizer.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StorageSynchronizerPropertiesConfiguration {

  @Autowired
  @Bean
  StorageSynchronizerProperties storageSynchronizerProperties(@Value("${kraken.storage.synchronizer.update-filter:#{environment.KRAKEN_STORAGE_SYNC_UPDATE_FILTER}}") final String updateFilter,
                                                              @Value("${kraken.storage.synchronizer.download-folder:#{environment.KRAKEN_STORAGE_SYNC_DOWNLOAD_FOLDER}}") final String downloadFolder) {

    log.info("Download folder is set to " + downloadFolder);
    log.info("Update filter is set to " + updateFilter);

    return StorageSynchronizerProperties.builder()
        .downloadFolder(downloadFolder)
        .updateFilter(updateFilter)
        .build();
  }
}