package com.kraken.runtime.docker;

import com.kraken.runtime.entity.ContainerStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerStatusToNameTest {

  private final ContainerStatusToName containerStatusToName = new ContainerStatusToName();

  @Test
  public void shouldConvert(){
    assertThat(containerStatusToName.apply("ID", ContainerStatus.RUNNING)).isEqualTo("ID_RUNNING");
  }
}

