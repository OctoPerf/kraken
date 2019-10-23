package com.kraken.tools.unique.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
final class Lang3IdGenerator implements IdGenerator {

  private static final int LENGTH = 10;

  @Override
  public int length() {
    return Lang3IdGenerator.LENGTH;
  }

  @Override
  public String generate() {
    return RandomStringUtils.randomAlphanumeric(this.length()).toLowerCase();
  }

}
