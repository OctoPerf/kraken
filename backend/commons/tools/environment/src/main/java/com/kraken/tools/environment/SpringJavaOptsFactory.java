package com.kraken.tools.environment;

import com.google.common.base.Preconditions;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SpringJavaOptsFactory implements JavaOptsFactory {

  @Override
  public String apply(Map<String, String> environment) {
    Preconditions.checkArgument(environment.isEmpty() || !environment
        .keySet()
        .stream()
        .anyMatch(key -> !key.matches("[a-zA-Z_]+[a-zA-Z0-9_]*")), "Environment variable names must match the regular expression [a-zA-Z_]{1,}[a-zA-Z0-9_]{0,}");

    // Reason for this: https://stackoverflow.com/questions/38349325/throws-error-when-passing-argument-with-space-in-java-opts-in-linux-os
    Preconditions.checkArgument(environment.isEmpty() || !environment
        .values()
        .stream()
        .anyMatch(value -> !value.matches("\\S+")), "Environment variable values cannot contain spaces.");

    return environment
        .entrySet()
        .stream()
        .map(entry -> String.format("-D%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining(" "));
  }
}
