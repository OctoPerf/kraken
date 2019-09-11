package com.kraken.storage.synchronizer;

import com.kraken.tools.configuration.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
final class StringToFileTransfer implements Function<String, FileTransfer> {

  @NonNull ApplicationProperties applicationProperties;

  @Override
  public FileTransfer apply(String str) {
    final var array = str.split("<->", 2);

    return FileTransfer.builder()
        .localPath(applicationProperties.getData().resolve(array[0]))
        .remotePath(array[1])
        .build();
  }
}
