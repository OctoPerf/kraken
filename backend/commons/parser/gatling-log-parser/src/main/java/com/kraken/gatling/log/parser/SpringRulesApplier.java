package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.rule.ParserRule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
class SpringRulesApplier implements RulesApplier {

  @NonNull
  List<ParserRule> rules;

  @Override
  public Optional<DebugEntry> apply(String line) {
    for (ParserRule rule : rules) {
      if (rule.test(line)) {
        return rule.apply(line);
      }
    }
    return Optional.empty();
  }
}
