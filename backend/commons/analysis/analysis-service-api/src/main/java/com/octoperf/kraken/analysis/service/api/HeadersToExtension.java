package com.octoperf.kraken.analysis.service.api;

import com.octoperf.kraken.analysis.entity.HttpHeader;

import java.util.List;
import java.util.function.Function;

public interface HeadersToExtension extends Function<List<HttpHeader>, String> {

}
