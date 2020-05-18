package com.kraken.security.decoder.spring;

import com.google.common.collect.ImmutableList;
import com.kraken.Application;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenRole;
import com.kraken.security.entity.token.KrakenTokenUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringTokenDecoderTest {

  @Autowired
  TokenDecoder decoder;

  @Test
  public void shouldDecodeAccessToken() throws IOException {
    final var user = decoder.decode("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZb19LT2IzeUZncGlzM05tT1F2OFE0N2ZJQlltbkpsZEtlRE1LQ1lBQThjIn0.eyJleHAiOjE1ODgyNTE3OTIsImlhdCI6MTU4ODI1MTQ5MiwianRpIjoiMmM3ZGRlZDQtZTllYi00ZTgyLWEyMzgtODk2NTc5ZjcyNDBmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL2tyYWtlbiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyZTQ0ZmZhZS0xMTFjLTRmNTktYWUyYi02NTAwMGRlNmY3YjciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJrcmFrZW4td2ViIiwic2Vzc2lvbl9zdGF0ZSI6ImIwYWQ0ZmJlLWQzMGQtNGJmMC1iMTAyLWM0NmYxMDVhMTBlNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlVTRVIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjdXJyZW50X2dyb3VwIjoiL2RlZmF1bHQta3Jha2VuIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJLcmFrZW4gVXNlciIsInByZWZlcnJlZF91c2VybmFtZSI6ImNvbnRhY3RAb2N0b3BlcmYuY29tIiwiZ2l2ZW5fbmFtZSI6IktyYWtlbiIsImZhbWlseV9uYW1lIjoiVXNlciIsImVtYWlsIjoiY29udGFjdEBvY3RvcGVyZi5jb20iLCJ1c2VyX2dyb3VwcyI6WyIvZGVmYXVsdC1rcmFrZW4iXX0.JzhagQ27346NngHPFD7aclVxXlJupCLeXmNNE8Qth8CvlsNhdHilJ38NYph9j64mWLMrW-bol0Pgf3xZ5WwVNbCnjpKIZnFxhcYiZMgvW3fM9DepC7UqyrEUl04Y39Y72bODjNDhE6CPrD1tkw4BUJts-EtbA6CawwGofkBXUynTuFoHhTKrkNRIdTN_7rvktHPOm3hFl5pVHoWCLLIOScCf2FK58mj4FXLN__rOgtEVIUAXUKrDgL3CsHzDsmKrrylOdzo4TLB04nptJRBUlSNdC1Qi8gNdNX3_54Aeq8O2tflbw6ufUR3LVVA_p7Ua6s0bvyuPFMJ7XCnv0AtkMw");
    Assertions.assertThat(user)
        .isEqualTo(
            KrakenTokenUser.builder()
                .username("contact@octoperf.com")
                .email("contact@octoperf.com")
                .userId("2e44ffae-111c-4f59-ae2b-65000de6f7b7")
                .roles(ImmutableList.of(KrakenRole.USER))
                .groups(ImmutableList.of("/default-kraken"))
                .currentGroup("/default-kraken")
                .issuedAt(Instant.ofEpochSecond(1588251492))
                .expirationTime(Instant.ofEpochSecond(1588251792))
                .build()
        );
  }

  @Test
  public void shouldDecodeRefreshToken() throws IOException {
    final var user = decoder.decode("eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4MGY1ZTAyMS04M2MxLTQzNzUtOWE4YS1kNTFlNzI4ZDQ5MWQifQ.eyJleHAiOjE1ODgyNTMyOTIsImlhdCI6MTU4ODI1MTQ5MiwianRpIjoiM2RiNzBiMzQtODFmNC00OGEzLWIxY2UtY2EyM2Q1YmMxMDc0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL2tyYWtlbiIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA4MC9hdXRoL3JlYWxtcy9rcmFrZW4iLCJzdWIiOiIyZTQ0ZmZhZS0xMTFjLTRmNTktYWUyYi02NTAwMGRlNmY3YjciLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoia3Jha2VuLXdlYiIsInNlc3Npb25fc3RhdGUiOiJiMGFkNGZiZS1kMzBkLTRiZjAtYjEwMi1jNDZmMTA1YTEwZTUiLCJzY29wZSI6ImVtYWlsIHByb2ZpbGUifQ.JNlM-t0ST59ZHO1Ua9yBqmyqKhSEw6lpJOs4RvCS6ts");
    Assertions.assertThat(user)
        .isEqualTo(
            KrakenTokenUser.builder()
                .username("")
                .email("")
                .userId("2e44ffae-111c-4f59-ae2b-65000de6f7b7")
                .roles(ImmutableList.of())
                .groups(ImmutableList.of())
                .currentGroup("")
                .issuedAt(Instant.ofEpochSecond(1588251492))
                .expirationTime(Instant.ofEpochSecond(1588253292))
                .build()
        );
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToDecodeMalformattedToken() throws IOException {
    decoder.decode("abc");
  }

  @Test(expected = IOException.class)
  public void shouldFailToDecodeMalformattedJSon() throws IOException {
    decoder.decode("asd.bqsdqs.cqsdqsd");
  }


  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToDecodeGroupMismatch() throws IOException {
    decoder.decode("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZb19LT2IzeUZncGlzM05tT1F2OFE0N2ZJQlltbkpsZEtlRE1LQ1lBQThjIn0.eyJleHAiOjE1ODU4Mzc0ODYsImlhdCI6MTU4NTgzNzE4NiwianRpIjoiN2VkMDg3NzYtNjE5NC00YjlkLTk5MzktYzljZGI4MTc3ZWQyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL2tyYWtlbiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyZTQ0ZmZhZS0xMTFjLTRmNTktYWUyYi02NTAwMGRlNmY3YjciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJrcmFrZW4td2ViIiwic2Vzc2lvbl9zdGF0ZSI6IjY3MDQ3MTNiLTQ1YTYtNGRmYS05ZDBiLTI0MTZjNzMyMTU3ZCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlVTRVIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjdXJyZW50X2dyb3VwIjoiL290aGVyLWdyb3VwIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6ImtyYWtlbi11c2VyIiwidXNlcl9ncm91cHMiOlsiL2RlZmF1bHQta3Jha2VuIl19.EL-VlLgpu7uzil7Ucy6R02cGXcrxkv-mq53jSDqgRYvZEwECrYf-IYzN2vJlSyxeu6c5QWogmtngaisMnMBmSKa4mnHCw0dtp5ie_OosEsWN7HDkAMNMJLfCfIq7sgGZvpFl2muSc6TNbwhM4OSQSOC_jtQdQNhHggQ1tMOXpJ5rWGCYizMyblnDrjUB0udYsX1eUZYUnMms_FX8YIrxw9Yq72y0YTXPdwEvaDX_u28gcSrnMbgHdETsEIQkmtVWQ2rN-BEQeVk50bpiWnf5OKS2T7XoK9R2Mz1z__ycSh5BxKiNY6yyz-9sdlRALNEQbKRMGhvcq5reIIJwZ1rWxg\n");
  }
}