package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.HostService;
import com.kraken.runtime.entity.Host;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController()
@RequestMapping("/host")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HostController {

  @NonNull HostService hostService;

  @GetMapping(value = "/list")
  public Flux<Host> list() {
    return hostService.list();
  }
}
