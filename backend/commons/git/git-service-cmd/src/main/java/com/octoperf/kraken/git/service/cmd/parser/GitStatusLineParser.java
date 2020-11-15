package com.octoperf.kraken.git.service.cmd.parser;

import com.octoperf.kraken.git.entity.GitStatus;

import java.util.function.BiFunction;

interface GitStatusLineParser extends BiFunction<GitStatus, String[], GitStatus> {

  Character getChar();

}
