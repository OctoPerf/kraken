package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.entity.owner.Owned;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GitLog implements Owned {

  Owner owner;
  String text;

  @JsonCreator
  GitLog(
      @NonNull @JsonProperty("owner") final Owner owner,
      @NonNull @JsonProperty("text") final String text
  ) {
    super();
    this.owner = owner;
    this.text = text;
  }

}
