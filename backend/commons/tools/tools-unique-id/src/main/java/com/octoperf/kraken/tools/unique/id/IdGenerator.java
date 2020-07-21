package com.octoperf.kraken.tools.unique.id;

// TODO split this in two modules: tools-unique-id-api and tools-unique-id-lang3
public interface IdGenerator {

  int length();

  String generate();

}
