package com.kraken.grafana.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GrafanaClientProperties {

  @NonNull
  String grafanaUrl;
  @NonNull
  String grafanaDashboard;
  @NonNull
  String grafanaUser;
  @NonNull
  String grafanaPassword;

}
