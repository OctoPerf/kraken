package com.octoperf.kraken.git.event;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.event.bus.BusEvent;

public interface GitEvent extends BusEvent {
  Owner getOwner();
}
