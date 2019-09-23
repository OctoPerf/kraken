package com.kraken.runtime.entity;

public enum ContainerStatus {
  CREATING,
  STARTING,
  PREPARING,
  READY,
  RUNNING,
  STOPPING,
  DONE;

  public static ContainerStatus parse(final String str) {
    final var underscoreIndex = str.lastIndexOf("_");
    return ContainerStatus.valueOf(str.substring(underscoreIndex + 1));
  }
}

