package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
@SuppressWarnings("squid:S2068")
public class GrafanaUser {

  public static final String EMAIL_ATTRIBUTE = "dashboardEmail";
  public static final String USER_ID_ATTRIBUTE = "dashboardUserId";
  public static final String PASSWORD_ATTRIBUTE = "dashboardPassword";
  public static final String DATASOURCE_NAME_ATTRIBUTE = "dashboardDatasourceName";
  public static final String ORGANIZATION_ID = "dashboardOrgId";

  String id;
  String email;
  String password;
  String datasourceName;
  String orgId;


  public GrafanaUser(@NonNull final String id,
                     @NonNull final String email,
                     @NonNull final String password,
                     @NonNull final String datasourceName,
                     @NonNull final String orgId) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.datasourceName = datasourceName;
    this.orgId = orgId;
  }
}