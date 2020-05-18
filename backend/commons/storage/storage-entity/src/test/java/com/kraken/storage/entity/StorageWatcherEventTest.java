package com.kraken.storage.entity;

import com.google.common.testing.NullPointerTester;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tests.utils.TestUtils.shouldPassEquals;
import static com.kraken.tests.utils.TestUtils.shouldPassToString;

public class StorageWatcherEventTest {

  public static final StorageWatcherEvent STORAGE_WATCHER_EVENT = StorageWatcherEvent.builder()
      .event("Event")
      .node(StorageNodeTest.STORAGE_NODE)
      .build();

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester().setDefault(StorageNode.class, StorageNodeTest.STORAGE_NODE).testConstructors(STORAGE_WATCHER_EVENT.getClass(), PACKAGE);
    shouldPassEquals(STORAGE_WATCHER_EVENT.getClass());
    shouldPassToString(STORAGE_WATCHER_EVENT);
    shouldPassToString(StorageWatcherEvent.builder());
  }

}
