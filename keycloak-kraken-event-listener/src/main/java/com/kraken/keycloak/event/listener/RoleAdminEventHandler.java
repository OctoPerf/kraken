package com.kraken.keycloak.event.listener;

import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class RoleAdminEventHandler implements AdminEventHandler {

  private final OperationType operationType;

  public RoleAdminEventHandler(OperationType operationType) {
    this.operationType = operationType;
  }

  @Override
  public String toPath(String url, AdminEvent event) {
    return String.format("%s/admin/%s_role", url, operationType.name().toLowerCase());
  }

  @Override
  public Entity<Form> toEntity(final AdminEvent event) {
    final Form form = new Form()
        .param("user_id", toUserId(event).orElseThrow(IllegalArgumentException::new))
        .param("role", toRole(event).orElseThrow(IllegalArgumentException::new));
    return Entity.form(form);
  }

  private Optional<String> toRole(final AdminEvent event) {
    // Admin Event Occurred:operationType=CREATE, realmId=master, clientId=c484a632-e20a-475b-b409-58abde4be8a0, userId=46be0a48-6373-4b24-8ea0-7167ae71db2a, ipAddress=127.0.0.1, resourcePath=users/a83b98f2-2c52-40f3-a3a3-2276caeacd3b/role-mappings/realm, representation=[{"id":"926f97ca-801b-4c38-8a57-1608575f80b2","name":"ADMIN","composite":false,"clientRole":false,"containerId":"kraken"}], resourceType=REALM_ROLE_MAPPING
    final String representation = event.getRepresentation();
    final Pattern pattern = Pattern.compile("\"name\":\"([^\"]+)\"");
    final Matcher matcher = pattern.matcher(representation);
    if (matcher.find()) {
      return Optional.ofNullable(matcher.group(1));
    }
    return Optional.empty();
  }

  @Override
  public boolean test(final AdminEvent event) {
    return operationType.equals(event.getOperationType())
        && ResourceType.REALM_ROLE_MAPPING.equals(event.getResourceType())
        && toUserId(event).isPresent()
        && toRole(event).isPresent();
  }
}
