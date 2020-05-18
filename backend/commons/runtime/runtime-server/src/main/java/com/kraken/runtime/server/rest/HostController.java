package com.kraken.runtime.server.rest;

import com.kraken.runtime.backend.api.HostService;
import com.kraken.runtime.entity.host.Host;
import com.kraken.security.authentication.api.UserProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

@Slf4j
@RestController()
@RequestMapping("/host")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HostController {

  @NonNull HostService hostService;
  @NonNull UserProvider userProvider;

  @GetMapping(value = "/list")
  public Flux<Host> list(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    return userProvider.getOwner(applicationId).flatMapMany(hostService::list);
  }

  @GetMapping(value = "/all")
  public Flux<Host> listAll() {
    return hostService.listAll();
  }

  @PostMapping(value = "/attach")
  public Mono<Host> attach(@RequestBody() final Host host) {
    log.info(String.format("Attach host %s", host.getName()));
    return hostService.attach(host);
  }

  @PostMapping(value = "/detach")
  public Mono<Host> detach(@RequestBody() final Host host) {
    log.info(String.format("Detach host %s", host.getName()));
    return hostService.detach(host);
  }

}
