package com.octoperf.kraken.storage.entity;

import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class StorageWatcherEventTest {

  public static final StorageWatcherEvent STORAGE_WATCHER_EVENT = StorageWatcherEvent.builder()
      .type(com.octoperf.kraken.storage.entity.StorageWatcherEventType.CREATE)
      .node(StorageNodeTest.STORAGE_NODE)
      .owner(PublicOwner.INSTANCE)
      .build();

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester().setDefault(StorageNode.class, StorageNodeTest.STORAGE_NODE).testConstructors(STORAGE_WATCHER_EVENT.getClass(), PACKAGE);
    TestUtils.shouldPassEquals(STORAGE_WATCHER_EVENT.getClass());
    TestUtils.shouldPassToString(STORAGE_WATCHER_EVENT);
    TestUtils.shouldPassToString(StorageWatcherEvent.builder());
  }

}
