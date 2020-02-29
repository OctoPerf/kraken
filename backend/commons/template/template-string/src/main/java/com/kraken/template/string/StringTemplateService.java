package com.kraken.template.string;


import com.kraken.template.api.TemplateService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
final class StringTemplateService implements TemplateService {

  @Override
  public Mono<String> replaceAll(final String input, final Map<String, String> context) {
    return Flux.fromIterable(context.entrySet())
        .reduce(input, (str, replacement) -> this.replace(str, replacement.getKey(), replacement.getValue()));
  }

  private String replace(final String input, final String from, final String to) {
    return input.replaceAll(Pattern.quote(String.format("${%s}", from)), to);
  }
}
