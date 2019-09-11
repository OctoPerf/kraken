package com.kraken.storage.synchronizer;

import org.junit.Test;

import java.nio.file.Path;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class FileTransferTest {

  public static final FileTransfer FILE_TRANSFER = FileTransfer.builder()
      .localPath(Path.of("foo"))
      .remotePath("bar")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(FILE_TRANSFER);
  }

}
