package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
public class GrafanaUser {

  public static String EMAIL_ATTRIBUTE = "dashboardEmail";
  public static String USER_ID_ATTRIBUTE = "dashboardUserId";
  public static String PASSWORD_ATTRIBUTE = "dashboardPassword";
  public static String DATASOURCE_NAME_ATTRIBUTE = "dashboardDatasourceName";
  public static String ORGANIZATION_ID = "dashboardOrgId";

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