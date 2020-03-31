package com.kraken.config.kubernetes.spring;

import com.kraken.config.kubernetes.api.KubernetesClientBuilderType;
import com.kraken.config.kubernetes.api.KubernetesProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Optional;

import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
import static java.util.regex.Matcher.quoteReplacement;

@Value
@ConstructorBinding
@ConfigurationProperties("kraken.k8s")
final class SpringKubernetesProperties implements KubernetesProperties {
  String namespace;
  boolean debug;
  boolean patchHosts;
  String pretty;
  KubernetesClientBuilderType builderType;
  Optional<String> configPath;

  @Builder
  SpringKubernetesProperties(
    final String namespace,
    final boolean debug,
    final boolean patchHosts,
    final String pretty,
    final KubernetesClientBuilderType builderType,
    final String configPath) {
    super();
    this.namespace = namespace;
    this.debug = debug;
    this.patchHosts = patchHosts;
    this.pretty = pretty;
    this.builderType = builderType;
    this.configPath = ofNullable(configPath)
      .map(path -> path.replaceFirst("^~", quoteReplacement(getProperty("user.home"))));
  }
}
