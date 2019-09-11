package com.kraken.storage.synchronizer;

import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class StorageSynchronizer {

  @NonNull StorageClient storageClient;

  @PostConstruct
  public void init() throws Exception {
//    TODO Créer un mock de runtime-server ?
//    TODO set status
    System.out.println("EHO !!!");
//    storageClient.downloadFile()
    System.out.println(storageClient.getContent("README.md").block());
    Thread.sleep(10000);
    System.out.println("OHE !!!");
  }

}
