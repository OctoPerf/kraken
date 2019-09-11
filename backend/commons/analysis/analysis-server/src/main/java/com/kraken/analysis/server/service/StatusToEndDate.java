package com.kraken.analysis.server.service;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.HttpHeader;
import com.kraken.analysis.entity.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class StatusToEndDate implements Function<ResultStatus, Long> {

  private static final Supplier<Long> END_DATE_NOW = () -> new Date().getTime();
  private static final Map<ResultStatus, Supplier<Long>> MAP = ImmutableMap.of(
      ResultStatus.STARTING, () -> 0L,
      ResultStatus.RUNNING, () -> 0L,
      ResultStatus.COMPLETED, END_DATE_NOW,
      ResultStatus.CANCELED, END_DATE_NOW,
      ResultStatus.FAILED, END_DATE_NOW);

  @Override
  public Long apply(final ResultStatus status) {
    return MAP.get(status).get();
  }

}
