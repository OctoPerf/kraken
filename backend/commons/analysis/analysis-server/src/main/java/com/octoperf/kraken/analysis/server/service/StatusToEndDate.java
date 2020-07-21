package com.octoperf.kraken.analysis.server.service;

import com.octoperf.kraken.analysis.entity.ResultStatus;

import java.util.function.Function;

interface StatusToEndDate extends Function<ResultStatus, Long> {

}
