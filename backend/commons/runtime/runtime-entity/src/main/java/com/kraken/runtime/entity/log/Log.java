package com.kraken.runtime.entity.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.security.entity.owner.Owned;
import com.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class Log implements Owned {
  Owner owner;
  String id;
  LogType type;
  String text;
  LogStatus status;

  @JsonCreator
  Log(
      @NonNull @JsonProperty("owner") final Owner owner,
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("type") final LogType type,
      @NonNull @JsonProperty("text") final String text,
      @NonNull @JsonProperty("status") final LogStatus status) {
    super();
    this.owner = owner;
    this.id = id;
    this.type = type;
    this.text = text;
    this.status = status;
  }
}
