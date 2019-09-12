package com.kraken.storage.synchronizer;

import com.kraken.tools.configuration.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

// localPath<->remotePath,localPath<->remotePath
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
final class StringToFileTransfer implements Function<String, List<FileTransfer>> {

  @NonNull ApplicationProperties applicationProperties;

  @Override
  public List<FileTransfer> apply(@Nonnull String str) {
    return Arrays.stream(str.split(",")).map(String::trim).map(s -> {
      final var array = s.split("<->", 2);
      return FileTransfer.builder()
          .localPath(applicationProperties.getData().resolve(array[0]))
          .remotePath(array[1])
          .build();
    }).collect(Collectors.toList());

  }
}
