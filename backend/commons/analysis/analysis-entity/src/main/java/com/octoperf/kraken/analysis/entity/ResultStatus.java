package com.octoperf.kraken.analysis.entity;

public enum ResultStatus {
  STARTING(false),
  RUNNING(false),
  STOPPING(false),
  COMPLETED(true),
  FAILED(true),
  CANCELED(true);

  ResultStatus(boolean terminal) {
    this.terminal = terminal;
  }

  protected boolean terminal;

  public boolean isTerminal() {
    return this.terminal;
  }
}
