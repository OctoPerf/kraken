package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.ResultStatus;

import java.util.function.Function;

interface StatusToEndDate extends Function<ResultStatus, Long> {

}
