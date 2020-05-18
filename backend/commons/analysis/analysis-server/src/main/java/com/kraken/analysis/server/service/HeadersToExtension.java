package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.HttpHeader;

import java.util.List;
import java.util.function.Function;

interface HeadersToExtension extends Function<List<HttpHeader>, String> {

}
