package com.kraken.commons.command.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class CommandClientProperties {

  @NonNull
  String commandUrl;

}
