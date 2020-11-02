package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;

import java.util.Map;
import java.util.function.Function;

public interface UserIdToCommandEnvironment extends Function<String, Map<KrakenEnvironmentKeys, String>> {
}
