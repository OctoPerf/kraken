package com.octoperf.kraken.parser.gatling.log.spring;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.rule.ParserRule;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringRulesApplier implements RulesApplier {

  List<ParserRule> rules;

  public SpringRulesApplier(@NonNull final List<ParserRule> rules) {
    this.rules = rules;
    this.rules.sort(Comparator.comparingInt(ParserRule::order));
  }

  @Override
  public Optional<DebugEntry> apply(String line) {
    log.debug(line);
    return rules.stream()
        .filter(rule -> rule.test(line))
        .findFirst()
        .map(rule -> {
          log.debug("Rule found: " + rule.getClass().getSimpleName());
          return rule.apply(line);
        })
        .orElse(Optional.empty());
  }
}
