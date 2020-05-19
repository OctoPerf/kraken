package com.kraken.security.admin.client.keycloak;

import com.kraken.Application;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class KeycloakSecurityAdminClientConfigurationTest {

  @Autowired
  SecurityAdminClient securityAdminClient;

  @Test
  public void shouldInject(){
    Assertions.assertThat(securityAdminClient).isNotNull();
  }

}