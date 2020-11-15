package com.octoperf.kraken.git.service.cmd;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GitSubCommand {
  ADD(false, false),
  COMMIT(false, false),
  FETCH(false, true),
  STATUS(false, false),
  BRANCH(true, true),
  CHECKOUT(true, true),
  MERGE(true, true),
  PULL(true, true),
  PUSH(true, true),
  REBASE(true, true),
  RM(true, false),
  LOG(true, true),
  DIFF(true, true),
  HELP(false, false);

 boolean refresh;
 boolean remote;
}
