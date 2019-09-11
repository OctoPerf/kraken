package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.ResultStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {StatusToEndDate.class})
public class StatusToEndDateTest {

  @Autowired
  Function<ResultStatus, Long> statusToEndDate;

  @Test
  public void shouldReturnEndDateForAllStatuses() {
    Arrays.asList(ResultStatus.values()).forEach(resultStatus -> assertThat(statusToEndDate.apply(resultStatus)).isNotNull());
  }

}
