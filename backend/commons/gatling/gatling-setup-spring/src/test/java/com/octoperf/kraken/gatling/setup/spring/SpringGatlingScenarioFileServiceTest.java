package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.config.api.LocalRemoteProperties;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.gatling.api.GatlingSimulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpringGatlingScenarioFileServiceTest {

  @Mock
  GatlingProperties gatlingProperties;
  @Mock
  GatlingSimulation simulation;
  @Mock
  LocalRemoteProperties localRemote;

  SpringGatlingSetupFileService fileService;

  @BeforeEach
  public void setUp() {
    given(gatlingProperties.getSimulation()).willReturn(simulation);
    given(gatlingProperties.getUserFiles()).willReturn(localRemote);
    given(simulation.getName()).willReturn("com.test.MySimulation");
    given(localRemote.getLocal()).willReturn("testDir");
    fileService = new SpringGatlingSetupFileService(gatlingProperties);
  }

  @Test
  public void shouldSaveLoadSimulation() {
    assertThat(fileService.loadSimulation().block())
        .isNotNull()
        .isEqualTo("Test");

    fileService.saveSimulation("Updated").block();

    assertThat(fileService.loadSimulation().block())
        .isNotNull()
        .isEqualTo("Updated");

    fileService.saveSimulation("Test").block();
  }
}