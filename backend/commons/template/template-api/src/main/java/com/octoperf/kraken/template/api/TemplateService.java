package com.octoperf.kraken.template.api;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface TemplateService {
  Mono<String> replaceAll(String input, Map<String, String> context);
}
