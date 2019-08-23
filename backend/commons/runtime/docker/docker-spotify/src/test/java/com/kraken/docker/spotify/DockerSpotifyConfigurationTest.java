package com.kraken.docker.spotify;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;

public class DockerSpotifyConfigurationTest {

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerSpotifyConfiguration.class);
  }

}
