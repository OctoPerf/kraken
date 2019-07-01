package com.kraken.commons.analysis.entity;

public enum ResultType {
  RUN(false),
  DEBUG(true),
  HAR(true);

  ResultType(boolean debug) {
    this.debug = debug;
  }

  protected boolean debug;

  public boolean isDebug() {
    return this.debug;
  }
}
