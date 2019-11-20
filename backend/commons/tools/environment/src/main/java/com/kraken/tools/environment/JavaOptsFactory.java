package com.kraken.tools.environment;

import java.util.Map;
import java.util.function.Function;

public interface JavaOptsFactory extends Function<Map<String, String>, String> {
}
