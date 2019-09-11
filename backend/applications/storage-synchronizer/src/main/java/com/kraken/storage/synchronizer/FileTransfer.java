package com.kraken.storage.synchronizer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class FileTransfer {

  @NonNull String remotePath;
  @NonNull Path localPath;

}
