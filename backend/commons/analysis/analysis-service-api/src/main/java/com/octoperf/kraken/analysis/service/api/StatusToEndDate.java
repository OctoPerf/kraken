package com.octoperf.kraken.analysis.service.api;

import com.octoperf.kraken.analysis.entity.ResultStatus;

import java.util.function.Function;

public interface StatusToEndDate extends Function<ResultStatus, Long> {

}
