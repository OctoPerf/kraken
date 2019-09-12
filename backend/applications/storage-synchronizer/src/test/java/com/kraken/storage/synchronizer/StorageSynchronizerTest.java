package com.kraken.storage.synchronizer;

import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ContainerTest;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.storage.client.StorageClient;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeTest;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.Optional;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StorageSynchronizerTest {

  @Mock StorageClient storageClient;
  @Mock RuntimeClient runtimeClient;
  RuntimeContainerProperties containerProperties;
  SynchronizerProperties synchronizerProperties;
  StorageSynchronizer synchronizer;

  @Before
  public void before(){
    containerProperties = RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
    synchronizerProperties = SynchronizerPropertiesTest.SYNCHRONIZER_PROPERTIES;
    synchronizer = new StorageSynchronizer(storageClient,
        runtimeClient,
        containerProperties,
        synchronizerProperties);

  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeContainerProperties.class, containerProperties)
        .setDefault(SynchronizerProperties.class, synchronizerProperties)
        .testConstructors(StorageSynchronizer.class, PACKAGE);
  }

  @Test
  public void shouldInit() {
    given(runtimeClient.setStatus(anyString(), any(ContainerStatus.class))).willReturn(Mono.just(ContainerTest.CONTAINER));
    given(runtimeClient.waitForStatus(anyString(), any(ContainerStatus.class))).willReturn(Mono.just(TaskTest.TASK));
    given(storageClient.downloadFile(any(Path.class), anyString())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.downloadFolder(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    synchronizer.init();
    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.STARTING);
    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.STOPPING);
    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);
    verify(runtimeClient).waitForStatus(containerProperties.getTaskId(), ContainerStatus.STOPPING);
    verify(storageClient).downloadFile(any(Path.class), anyString());
    verify(storageClient).downloadFolder(any(Path.class), any());
    verify(storageClient).uploadFile(any(Path.class), any());
  }
}
