package com.kraken.tools.environment;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringJavaOptsFactoryTest {

  SpringJavaOptsFactory factory;

  @Before
  public void before(){
    factory = new SpringJavaOptsFactory();
  }

  @Test
  public void shouldGenerateOpts1(){
    assertThat(factory.apply(ImmutableMap.of("foo", "bar", "test", "some value"))).isEqualTo("-Dfoo=\"bar\" -Dtest=\"some value\"");
  }

  @Test
  public void shouldGenerateOpts2(){
    assertThat(factory.apply(ImmutableMap.of("KRAKEN_VERSION", "1.3.0", "MyValue", "some value"))).isEqualTo("-DKRAKEN_VERSION=\"1.3.0\" -DMyValue=\"some value\"");
  }

  @Test
  public void shouldGenerateOpts3(){
    assertThat(factory.apply(ImmutableMap.of())).isEqualTo("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail1(){
    factory.apply(ImmutableMap.of("4foo", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail2(){
    factory.apply(ImmutableMap.of("fo o", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail3(){
    factory.apply(ImmutableMap.of("fo-o", "bar"));
  }
}
