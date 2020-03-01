package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.task.Container;
import com.kraken.runtime.entity.task.FlatContainer;

import java.util.function.Function;

public interface FlatContainerToContainer extends Function<FlatContainer, Container> {
}
