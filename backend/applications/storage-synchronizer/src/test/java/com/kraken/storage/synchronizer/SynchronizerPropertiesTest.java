package com.kraken.storage.synchronizer;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class SynchronizerPropertiesTest {

  public static final SynchronizerProperties SYNCHRONIZER_PROPERTIES = SynchronizerProperties.builder()
      .fileDownloads(ImmutableList.of(FileTransferTest.FILE_TRANSFER))
      .folderDownloads(ImmutableList.of(FileTransferTest.FILE_TRANSFER))
      .fileUploads(ImmutableList.of(FileTransferTest.FILE_TRANSFER))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(SYNCHRONIZER_PROPERTIES);
  }

}

