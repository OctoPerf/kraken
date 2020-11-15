package com.octoperf.kraken.runtime.entity.task;

public enum ContainerStatus {
  CREATING(false),
  STARTING(false),
  PREPARING(false),
  READY(false),
  RUNNING(false),
  STOPPING(false),
  FAILED(true),
  DONE(true);

  ContainerStatus(boolean terminal) {
    this.terminal = terminal;
  }

  protected boolean terminal;

  public boolean isTerminal() {
    return this.terminal;
  }

  public static ContainerStatus parse(final String str) {
    final var underscoreIndex = str.lastIndexOf('_');
    return ContainerStatus.valueOf(str.substring(underscoreIndex + 1));
  }
}

