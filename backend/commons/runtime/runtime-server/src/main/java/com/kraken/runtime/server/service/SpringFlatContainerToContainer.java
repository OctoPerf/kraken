package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.task.Container;
import com.kraken.runtime.entity.task.FlatContainer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringFlatContainerToContainer implements FlatContainerToContainer {

  @Override
  public Container apply(final FlatContainer container) {
    return Container.builder()
        .id(container.getId())
        .status(container.getStatus())
        .startDate(container.getStartDate())
        .label(container.getLabel())
        .name(container.getName())
        .hostId(container.getHostId())
        .build();
  }
}
