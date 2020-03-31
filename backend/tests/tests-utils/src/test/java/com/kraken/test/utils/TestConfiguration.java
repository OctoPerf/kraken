package com.kraken.test.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = {"com.kraken"}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern="com.kraken.*.*TestConfiguration"))
public class TestConfiguration {

}