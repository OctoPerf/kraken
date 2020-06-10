package com.octoperf.kraken.analysis.client.web;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.analysis.entity.ResultTest;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


// start keycloak and make serve-storage/runtime-docker/analysis before running
@Tag("integration")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class WebAnalysisClientIntegrationTest {

  @Autowired
  private AnalysisClientBuilder analysisClientBuilder;

  AnalysisClient analysisClient;

  @BeforeEach
  public void setUp(){
    analysisClient = analysisClientBuilder.applicationId("gatling").mode(AuthenticationMode.IMPERSONATE, "kraken-user").build().block();
  }

  @Test
  public void shouldCreateResult() {
    analysisClient.create(ResultTest.RESULT).block();
  }
}
