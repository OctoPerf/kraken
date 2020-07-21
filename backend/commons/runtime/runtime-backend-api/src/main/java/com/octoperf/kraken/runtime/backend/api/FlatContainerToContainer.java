package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.entity.task.Container;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;

import java.util.function.Function;

public interface FlatContainerToContainer extends Function<FlatContainer, Container> {
}
