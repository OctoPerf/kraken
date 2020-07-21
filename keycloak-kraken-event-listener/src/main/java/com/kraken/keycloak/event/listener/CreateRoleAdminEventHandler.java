package com.kraken.keycloak.event.listener;

import org.keycloak.events.admin.OperationType;

final class CreateRoleAdminEventHandler extends RoleAdminEventHandler {

  public CreateRoleAdminEventHandler(){
    super(OperationType.CREATE);
  }

}
