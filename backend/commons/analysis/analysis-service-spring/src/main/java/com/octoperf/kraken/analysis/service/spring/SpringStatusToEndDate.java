package com.octoperf.kraken.analysis.service.spring;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.service.api.StatusToEndDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

@Component
@Slf4j
final class SpringStatusToEndDate implements StatusToEndDate {

  private static final Supplier<Long> END_DATE_NOW = () -> new Date().getTime();
  private static final Supplier<Long> END_DATE_ZERO = () -> 0L;
  private static final Map<ResultStatus, Supplier<Long>> MAP = ImmutableMap.<ResultStatus, Supplier<Long>>builder()
      .put(ResultStatus.STARTING, END_DATE_ZERO)
      .put(ResultStatus.RUNNING, END_DATE_ZERO)
      .put(ResultStatus.STOPPING, END_DATE_ZERO)
      .put(ResultStatus.COMPLETED, END_DATE_NOW)
      .put(ResultStatus.CANCELED, END_DATE_NOW)
      .put(ResultStatus.FAILED, END_DATE_NOW)
      .build();

  @Override
  public Long apply(final ResultStatus status) {
    return MAP.get(status).get();
  }

}
