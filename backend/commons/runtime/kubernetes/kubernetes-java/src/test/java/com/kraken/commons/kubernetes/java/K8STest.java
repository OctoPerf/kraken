package com.kraken.commons.kubernetes.java;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Watch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Duration;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {K8SClientConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class K8STest {

//  TODO pouvoir lancer déploiement
//  TODO Afficher la liste des pods (namespace kraken) + filtre que les gatling

  @Autowired
  CoreV1Api api;

  @Autowired
  ApiClient client;

  @Test
  public void shouldListPods() throws ApiException {
    // invokes the CoreV1Api client
    V1PodList list = api.listPodForAllNamespaces(null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false);
    for (V1Pod item : list.getItems()) {
      System.out.println(item.getMetadata().getName());
    }
  }

  @Test
  public void shouldReturnLogs() throws ApiException {
    // invokes the CoreV1Api client
    V1PodList list = api.listPodForAllNamespaces(null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false);
    for (V1Pod item : list.getItems()) {
      System.out.println(item.getMetadata().getName());
    }

    // TODO start a pod
    // TODO return logs
  }

  @Test
  public void shouldWatchPods() throws IOException, ApiException {
    final int timeout = (int) Duration.ofDays(1).toSeconds();
    System.out.println(timeout);
    try (Watch<V1Pod> watch = Watch.createWatch(
        client,
        api.listPodForAllNamespacesCall(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            true,
            null,
            null),
        new TypeToken<Watch.Response<V1Pod>>() { }.getType())) {
      for (Watch.Response<V1Pod> item : watch) {
        System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
      }
    }
  }
}
