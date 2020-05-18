package com.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicOwner.class, name = "PUBLIC"),
    @JsonSubTypes.Type(value = ApplicationOwner.class, name = "APPLICATION"),
    @JsonSubTypes.Type(value = UserOwner.class, name = "USER")
})
public interface Owner {
  OwnerType getType();
}
