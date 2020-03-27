package com.kraken.runtime.gatling;

import com.kraken.runtime.gatling.api.GatlingLog;
import lombok.Data;

@Data
final class GatlingLogProp implements GatlingLog {
  String info = "";
  String debug = "";
}