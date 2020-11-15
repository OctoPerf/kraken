package com.octoperf.kraken.gatling.setup.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.gatling.api.GatlingScenario;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpringGatlingScenarioSimulationServiceTest {

  @Mock
  ContainerProperties containerProperties;
  @Mock
  GatlingProperties gatlingProperties;
  @Mock
  GatlingScenario setup;
  @Mock
  InjectionPolicyFactory policyFactory;

  SpringGatlingSetupSimulationService simulationService;

  @BeforeEach
  public void setUp() {
    given(gatlingProperties.getScenario()).willReturn(setup);
    simulationService = new SpringGatlingSetupSimulationService(containerProperties, gatlingProperties, ImmutableMap.of(TaskType.GATLING_DEBUG, policyFactory));
  }

  @Test
  public void shouldNotUpdate() {
    given(setup.getCustomSetup()).willReturn(true);
    assertThat(simulationService.update("content").block()).isNotNull().isEqualTo("content");
  }

  @Test
  public void shouldUpdateSimple() {
    given(setup.getCustomSetup()).willReturn(false);
    given(containerProperties.getTaskType()).willReturn(TaskType.GATLING_DEBUG);
    given(policyFactory.apply(1)).willReturn(ImmutableList.of(".inject(fromTest(42))"));
    assertThat(simulationService.update("        .exec(http(\"Product ${productId}\")\n" +
        "            .get(\"/actions/Catalog.action\")\n" +
        "            .queryParam(\"viewProduct\", \"\")\n" +
        "            .queryParam(\"productId\", \"${productId}\"))\n" +
        "\n" +
        "    setUp(scn.inject(constantConcurrentUsers(100) during(3 minutes))).protocols(httpProtocol)\n" +
        "}").block()).isNotNull().isEqualTo("        .exec(http(\"Product ${productId}\")\n" +
        "            .get(\"/actions/Catalog.action\")\n" +
        "            .queryParam(\"viewProduct\", \"\")\n" +
        "            .queryParam(\"productId\", \"${productId}\"))\n" +
        "\n" +
        "    setUp(scn.inject(fromTest(42))).protocols(httpProtocol)\n" +
        "}");
  }

  @Test
  public void shouldUpdate() {
    given(setup.getCustomSetup()).willReturn(false);
    given(containerProperties.getTaskType()).willReturn(TaskType.GATLING_DEBUG);
    given(policyFactory.apply(2)).willReturn(ImmutableList.of(".inject(fromTest(42))", ".inject(fromTest(1337))"));
    assertThat(simulationService.update("    setUp(PetStoreSimulationBuyer.scn.inject(constantConcurrentUsers(2) during(2 minutes)),\n" +
        "          PetStoreSimulationVisitor.scn.inject(constantConcurrentUsers(10) during(2 minutes))).protocols(httpProtocol)\n" +
        "}\n").block()).isNotNull().isEqualTo("    setUp(PetStoreSimulationBuyer.scn.inject(fromTest(42)),\n" +
        "          PetStoreSimulationVisitor.scn.inject(fromTest(1337))).protocols(httpProtocol)\n" +
        "}\n");
  }

  @Test
  public void shouldComplex() {
    given(setup.getCustomSetup()).willReturn(false);
    given(containerProperties.getTaskType()).willReturn(TaskType.GATLING_DEBUG);
    given(policyFactory.apply(2)).willReturn(ImmutableList.of(".inject(fromTest(42))", ".inject(fromTest(1337))"));
    assertThat(simulationService.update("    setUp(PetStoreSimulationBuyer.scn.InJeCt(constantConcurrentUsers(2) during(2 minutes)),\n" +
        "          PetStoreSimulationVisitor.scn." +
        "  inject" +
        "  (constantConcurrentUsers(10) during(2 minutes))).protocols(httpProtocol)\n" +
        "}\n").block()).isNotNull().isEqualTo("    setUp(PetStoreSimulationBuyer.scn.inject(fromTest(42)),\n" +
        "          PetStoreSimulationVisitor.scn.inject(fromTest(1337))).protocols(httpProtocol)\n" +
        "}\n");
  }

}