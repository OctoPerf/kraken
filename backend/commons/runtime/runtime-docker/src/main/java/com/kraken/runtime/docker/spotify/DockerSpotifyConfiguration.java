package com.kraken.runtime.docker.spotify;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DockerSpotifyConfiguration {

  @Bean
  DockerClient client() throws DockerCertificateException {
    return DefaultDockerClient.fromEnv().build();
  }
}
