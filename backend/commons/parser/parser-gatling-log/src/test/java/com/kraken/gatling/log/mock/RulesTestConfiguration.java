package com.kraken.gatling.log.mock;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.context.ParserContext;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.kraken.gatling.log.parser.rule"})
public class RulesTestConfiguration {

  @Bean
  public ParserContext context() {
    return Mockito.mock(ParserContext.class);
  }


  @Bean
  public DebugEntry.DebugEntryBuilder builder() {
    return Mockito.mock(DebugEntry.DebugEntryBuilder.class);
  }

}
