package com.kraken.storage.user.events.listener;

import com.kraken.config.api.ApplicationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StorageUserEventsServiceTest {

  @Mock
  ApplicationProperties properties;

  StorageUserEventsService service;
  Path data;

  @Before
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