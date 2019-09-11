package com.kraken.storage.synchronizer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
class SynchronizerProperties {

  @NonNull List<FileTransfer> fileDownloads;
  @NonNull List<FileTransfer> folderDownloads;
  @NonNull List<FileTransfer> fileUploads;
  @NonNull List<FileTransfer> folderUploads;

}

