package com.kraken.runtime.command;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class ZtCommandService implements CommandService {

  @NonNull
  Function<String, String> stringCleaner;

  @Override
  public Flux<String> execute(final Command command) {
    final var startedProcessRef = new AtomicReference<StartedProcess>();
    return Flux.<String>create(emitter -> {
      log.info(String.format("Executing command %s in path %s", String.join(" ", command.getCommand()), command.getPath()));
      final var file = Paths.get(command.getPath()).toFile();
      final var process = new ProcessExecutor().command(command.getCommand())
          .directory(file)
          .environment(command.getEnvironment())
          .redirectErrorStream(true)
          .redirectOutput(new LogOutputStream() {
            @Override
            protected void processLine(final String line) {
              emitter.next(line);
            }
          });
      try {
        final var startedProcess = process.start();
        startedProcessRef.set(startedProcess);
        startedProcess.getFuture().get();
      } catch (InterruptedException | ExecutionException | IOException e) {
        log.error("Command execution failed", e);
        emitter.error(e);
      }
      emitter.complete();
    })
        .doOnCancel(() -> startedProcessRef.get().getProcess().destroyForcibly())
        .map(stringCleaner);
  }


}
