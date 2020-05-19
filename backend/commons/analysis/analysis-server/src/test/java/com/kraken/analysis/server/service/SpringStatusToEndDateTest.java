package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.ResultStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {SpringStatusToEndDate.class})
public class SpringStatusToEndDateTest {

  @Autowired
  StatusToEndDate statusToEndDate;

  @Test
  public void shouldReturnEndDateForAllStatuses() {
    Arrays.asList(ResultStatus.values()).forEach(resultStatus -> assertThat(statusToEndDate.apply(resultStatus)).isNotNull());
  }

}
