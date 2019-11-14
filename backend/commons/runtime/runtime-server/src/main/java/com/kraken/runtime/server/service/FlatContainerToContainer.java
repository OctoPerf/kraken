package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.FlatContainer;

import java.util.function.Function;

public interface FlatContainerToContainer extends Function<FlatContainer, Container> {
}
