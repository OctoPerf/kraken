package com.octoperf.kraken.analysis.service.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.analysis.entity.HttpHeader;
import com.octoperf.kraken.analysis.service.api.HeadersToExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {SpringHeadersToExtension.class})
public class SpringHeadersToExtensionTest {

  @Autowired
  HeadersToExtension headersToExtension;

  @Test
  public void shouldReturnTxtNoContentType() {
    assertThat(headersToExtension.apply(ImmutableList.of())).isEqualTo(".txt");
  }

  @Test
  public void shouldReturnTxtUnknownContentType() {
    assertThat(headersToExtension.apply(ImmutableList.of(HttpHeader.builder().key("Content-Type").value("Ca-va-fail").build()))).isEqualTo(".txt");
  }

  @Test
  public void shouldReturnJsonNoContentType() {
    assertThat(headersToExtension.apply(ImmutableList.of(HttpHeader.builder().key("Content-Type").value("application/json;charset=UTF-8").build()))).isEqualTo(".json");
  }
}
