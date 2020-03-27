package com.kraken.runtime.gatling;

import com.kraken.runtime.gatling.api.GatlingLocalRemote;
import lombok.Data;

@Data
final class GatlingLocalRemoteProp implements GatlingLocalRemote {
  String local = "";
  String remote = "";
}