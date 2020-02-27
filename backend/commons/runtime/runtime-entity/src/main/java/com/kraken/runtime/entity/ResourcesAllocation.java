package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

/**
 * https://developers.redhat.com/blog/2017/03/14/java-inside-docker/
 * https://docs.docker.com/compose/compose-file/#resources
 * https://kubernetes.io/docs/tasks/configure-pod-container/assign-memory-resource/#specify-a-memory-request-and-a-memory-limit
 * https://kubernetes.io/docs/tasks/configure-pod-container/assign-cpu-resource/
 */
@Value
@Builder
public class ResourcesAllocation {
  String cpuRequest;
  String cpuLimit;
  String memoryRequest;
  String memoryLimit;

  @JsonCreator
  ResourcesAllocation(
      @JsonProperty("cpuRequest") final String cpuRequest,
      @JsonProperty("cpuLimit") final String cpuLimit,
      @JsonProperty("memoryRequest") final String memoryRequest,
      @JsonProperty("memoryLimit") final String memoryLimit) {
    super();
    this.cpuRequest = requireNonNull(cpuRequest);
    this.cpuLimit = requireNonNull(cpuLimit);
    this.memoryRequest = requireNonNull(memoryRequest);
    this.memoryLimit = requireNonNull(memoryLimit);
  }
}
