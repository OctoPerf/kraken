package com.octoperf.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.tools.sse.SSEService;
import com.octoperf.kraken.tools.sse.SSEWrapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.Map;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SESSION;

@Slf4j
@RestController()
@RequestMapping("/sse")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class SSEController {

  @NonNull SSEService sse;
  @NonNull Map<SSEChannel, SSEChannelBuilder> channelBuilders;

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<SSEWrapper>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                 @RequestHeader(value = "ProjectId", required = false) final String projectId,
                                                 @RequestParam("channel") final SSEChannel[] channels) {
    final var order = AuthenticatedClientBuildOrder.builder()
        .mode(SESSION)
        .applicationId(applicationId)
        .projectId(projectId)
        .build();

    final var builders = Arrays.stream(channels).map(channelBuilders::get);
    return Flux.fromStream(builders).flatMap(sseChannelBuilder -> sseChannelBuilder.apply(order)).collectList().flatMapMany(maps -> {
      final var reduced = maps.stream().reduce((stringFluxMap, stringFluxMap2) -> ImmutableMap.<String, Flux<? extends Object>>builder().putAll(stringFluxMap).putAll(stringFluxMap2).build())
          .orElse(ImmutableMap.of());
      return sse.keepAlive(sse.merge(reduced));
    }).map(event -> {
      log.debug(event.toString());
      return event;
    });
  }
}
