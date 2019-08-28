package com.kraken.analysis.server.service;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.entity.HttpHeader;
import com.kraken.analysis.server.service.HeadersToExtension;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {HeadersToExtension.class})
public class HeadersToExtensionTest {

  @Autowired
  Function<List<HttpHeader>, String> headersToExtension;

  @Test
  public void shouldReturnTxtNoContentType() {
    Assertions.assertThat(headersToExtension.apply(ImmutableList.of())).isEqualTo(".txt");
  }

  @Test
  public void shouldReturnTxtUnknownContentType() {
    Assertions.assertThat(headersToExtension.apply(ImmutableList.of(HttpHeader.builder().key("Content-Type").value("Ca-va-fail").build()))).isEqualTo(".txt");
  }

  @Test
  public void shouldReturnJsonNoContentType() {
    Assertions.assertThat(headersToExtension.apply(ImmutableList.of(HttpHeader.builder().key("Content-Type").value("application/json;charset=UTF-8").build()))).isEqualTo(".json");
  }
}
