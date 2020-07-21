package com.octoperf.kraken.storage.user.events.listener;

import com.octoperf.kraken.config.api.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StorageUserEventsServiceTest {

  @Mock
  ApplicationProperties properties;

  StorageUserEventsService service;
  Path data;

  @BeforeEach
  public void setUp() {
    service = new StorageUserEventsService(properties);
    given(properties.getData()).willReturn("testDir");
    data = Paths.get(properties.getData(), "users", "foo");
    assertThat(data.toFile().mkdirs()).isTrue();
  }

  @Test
  public void shouldDeleteUserFolder() {
    service.onDeleteUser("foo").block();
    assertThat(data.toFile().exists()).isFalse();
  }
}