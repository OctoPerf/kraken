package com.octoperf.kraken.security.configuration;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import com.octoperf.kraken.tests.web.security.JwtTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtConverterTest {

  @Mock
  TokenDecoder decoder;

  JwtConverter converter;

  @BeforeEach
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

  @Test
  public void shouldConvertFail() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      given(decoder.decode("token")).willThrow(IOException.class);
      final var jwt = JwtTestFactory.JWT_FACTORY.create("token", ImmutableList.of(KrakenRole.USER.name()),
          ImmutableList.of("/default-kraken"), Optional.empty());
      final var mono = converter.convert(jwt);
      assertThat(mono).isNotNull();
      mono.block();
    });
  }
}