package com.kraken.gatling.log.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.function.Function;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
final class PathToLines implements Function<Path, Flux<String>> {

  long pointer = 0;
  boolean running = true;

  @Override
  public Flux<String> apply(Path path) {
    return Flux.create(sink -> {
      sink.onDispose(() -> running = false);
      final var logFile = path.toFile();
      while (running) {
        try {
          if (!logFile.exists()) {
            // Wait for file to be created
            log.info("Waiting for file at " + path);
          } else {
            final RandomAccessFile br = new RandomAccessFile(logFile, "r");
            String line;
            br.seek(pointer);
            while ((line = br.readLine()) != null) {
              sink.next(line);
              pointer = br.getFilePointer();
            }
            log.info("Waiting " + br.length());
            br.close();
          }
          Thread.sleep(1000);
        } catch (InterruptedException i) {
          log.info("Log file reading interrupted");
          running = false;
          sink.complete();
        } catch (Exception e) {
          log.error("Error while reading log file", e);
          running = false;
          sink.error(e);
        }
      }
    });
  }
}
