package com.kraken.analysis.server;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController()
@RequestMapping("/result")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
class AnalysisController {

  @NonNull
  AnalysisService service;

  @PostMapping()
  public Mono<StorageNode> create(@RequestBody() final Result result) {
    return service.create(result);
  }

  @DeleteMapping("/delete")
  public Mono<String> delete(@RequestParam("resultId") final String resultId) {
    return service.delete(resultId);
  }

  @PostMapping("/status/{status}")
  public Mono<StorageNode> setStatus(@RequestParam("resultId") final String resultId, @PathVariable("status") final ResultStatus status) {
    return service.setStatus(resultId, status);
  }

  @PostMapping(value = "/debug")
  public Mono<DebugEntry> debug(@RequestBody() final DebugEntry debug) {
    return service.addDebug(debug);
  }

}
