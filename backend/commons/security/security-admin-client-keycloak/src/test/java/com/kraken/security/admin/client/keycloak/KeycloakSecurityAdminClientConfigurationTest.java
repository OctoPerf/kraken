package com.kraken.security.admin.client.keycloak;

import com.kraken.Application;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class KeycloakSecurityAdminClientConfigurationTest {

  @Autowired
  SecurityAdminClient securityAdminClient;

  @Test
  public void shouldInject(){
    Assertions.assertThat(securityAdminClient).isNotNull();
  }

}