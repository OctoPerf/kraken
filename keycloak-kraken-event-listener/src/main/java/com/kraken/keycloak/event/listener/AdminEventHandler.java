package com.kraken.keycloak.event.listener;

import org.keycloak.events.admin.AdminEvent;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface AdminEventHandler extends Predicate<AdminEvent> {

  Entity<Form> toEntity(final AdminEvent event);

  String toPath(String url, AdminEvent event);

  default Optional<String> toUserId(final AdminEvent event) {
    // Admin Event Occurred:operationType=CREATE, realmId=master, clientId=c484a632-e20a-475b-b409-58abde4be8a0, userId=46be0a48-6373-4b24-8ea0-7167ae71db2a, ipAddress=127.0.0.1, resourcePath=users/a83b98f2-2c52-40f3-a3a3-2276caeacd3b/role-mappings/realm, representation=[{"id":"926f97ca-801b-4c38-8a57-1608575f80b2","name":"ADMIN","composite":false,"clientRole":false,"containerId":"kraken"}], resourceType=REALM_ROLE_MAPPING
    // Admin Event Occurred:operationType=DELETE, realmId=master, clientId=c484a632-e20a-475b-b409-58abde4be8a0, userId=46be0a48-6373-4b24-8ea0-7167ae71db2a, ipAddress=127.0.0.1, resourcePath=users/0cb44ca3-0dd8-4fb9-8509-3feb1db3979b, representation=null, resourceType=USER
    final String[] split = event.getResourcePath().split("/");
    if (split.length >= 2) {
      return Optional.of(split[1]);
    }
    return Optional.empty();
  }
}
