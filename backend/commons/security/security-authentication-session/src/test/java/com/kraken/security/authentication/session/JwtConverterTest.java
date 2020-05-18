package com.kraken.security.authentication.session;

import com.google.common.collect.ImmutableList;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenRole;
import com.kraken.security.entity.token.KrakenTokenUserTest;
import com.kraken.tests.security.JwtTestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtConverterTest {

  @Mock
  TokenDecoder decoder;

  JwtConverter converter;

  @Before
  public void setUp() throws IOException {
    converter = new JwtConverter(decoder);
    given(decoder.decode("token")).willReturn(KrakenTokenUserTest.KRAKEN_USER);
  }

  @Test
  public void shouldConvert() {
    final var jwt = JwtTestFactory.JWT_FACTORY.create("token", ImmutableList.of(KrakenRole.USER.name()),
        ImmutableList.of("/default-kraken"), Optional.empty());
    final var result = converter.convert(jwt);
    assertThat(result).isNotNull();
    final var token = result.block();
    assertThat(token).isNotNull();
    assertThat(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableList())).isEqualTo(ImmutableList.of("USER"));
    assertThat(token.getDetails()).isEqualTo(KrakenTokenUserTest.KRAKEN_USER);
  }

  @Test(expected = RuntimeException.class)
  public void shouldConvertFail() throws IOException {
    given(decoder.decode("token")).willThrow(IOException.class);
    final var jwt = JwtTestFactory.JWT_FACTORY.create("token", ImmutableList.of(KrakenRole.USER.name()),
        ImmutableList.of("/default-kraken"), Optional.empty());
    final var mono = converter.convert(jwt);
    assertThat(mono).isNotNull();
    mono.block();
  }
}