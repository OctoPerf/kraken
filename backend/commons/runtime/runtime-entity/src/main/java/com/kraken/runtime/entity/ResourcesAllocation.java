package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

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
  Float cpuRequest;
  Float cpuLimit;
  Integer memoryRequest; // in MB
  Integer memoryLimit;
  Optional<Float> memoryPercentage; // [0,1]

  @JsonCreator
  ResourcesAllocation(
      @JsonProperty("cpuRequest") final Float cpuRequest,
      @JsonProperty("cpuLimit") final Float cpuLimit,
      @JsonProperty("memoryRequest") final Integer memoryRequest,
      @JsonProperty("memoryLimit") final Integer memoryLimit,
      @JsonProperty("memoryPercentage") final Optional<Float> memoryPercentage) {
    super();
    this.cpuRequest = requireNonNull(cpuRequest);
    this.cpuLimit = requireNonNull(cpuLimit);
    this.memoryRequest = requireNonNull(memoryRequest);
    this.memoryLimit = requireNonNull(memoryLimit);
    this.memoryPercentage = requireNonNull(memoryPercentage);
  }
}
