package com.kraken.keycloak.event.listener;

import org.keycloak.events.admin.OperationType;

final class DeleteRoleAdminEventHandler extends RoleAdminEventHandler {

  public DeleteRoleAdminEventHandler(){
    super(OperationType.DELETE);
  }

}
