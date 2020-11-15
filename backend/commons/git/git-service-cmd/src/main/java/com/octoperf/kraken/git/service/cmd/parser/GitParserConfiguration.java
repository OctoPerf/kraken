package com.octoperf.kraken.git.service.cmd.parser;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
class GitParserConfiguration {

  @Bean
  public Map<Character, GitStatusLineParser> parsers(@NonNull final List<GitStatusLineParser> parsers) {
    return parsers.stream().collect(Collectors.toMap(GitStatusLineParser::getChar, gitStatusLineParser -> gitStatusLineParser));
  }
}
