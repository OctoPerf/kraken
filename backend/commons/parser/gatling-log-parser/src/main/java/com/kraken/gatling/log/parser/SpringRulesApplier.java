package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.rule.ParserRule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
class SpringRulesApplier implements RulesApplier {

  @NonNull
  List<ParserRule> rules;

  @Override
  public Optional<DebugEntry> apply(String line) {
    log.info(line);
    for (ParserRule rule : rules) {
      if (rule.test(line)) {
        log.info("Rule found: " + rule.getClass().getSimpleName());
        return rule.apply(line);
      }
    }
    return Optional.empty();
  }
}
