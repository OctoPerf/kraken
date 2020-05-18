package com.kraken.har.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.analysis.entity.DebugEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
final class SpringHarParser implements HarParser {

  private static final String ENTRIES = "entries";

  @NonNull
  ObjectMapper mapper;
  @NonNull
  BiFunction<JsonNode, String, DebugEntry> entryToDebugChunk;
  @NonNull
  Function<String, Long> stringToTimestamp;

  @Override
  public Flux<DebugEntry> parse(final Path harFilePath) {
    return this.createNamesList(harFilePath).collectList().flatMapMany(names -> this.createDebugEntries(harFilePath, names));
  }

  private Flux<String> createNamesList(final Path harLocalPath) {
    final var harEntryCount = new AtomicInteger(0);
    final var requestCount = new AtomicInteger(0);
    return this.parseHar(harLocalPath)
        .map(entryNode -> {
          final var dateStr = entryNode.get("startedDateTime").asText();
          final var timestamp = this.stringToTimestamp.apply(dateStr);
          final var inc = harEntryCount.incrementAndGet();
          return new HarEntry(timestamp, inc, "");
        })
        .sort(Comparator.comparingLong(HarEntry::getTimestamp))
        .map(harEntry -> harEntry.withName(String.format("request_%d", requestCount.getAndIncrement())))
        .sort(Comparator.comparingInt(HarEntry::getIndex))
        .map(HarEntry::getName);
  }

  private Flux<DebugEntry> createDebugEntries(final Path harLocalPath, final List<String> names) {
    final var count = new AtomicInteger(0);
    return this.parseHar(harLocalPath)
        .map(entryNode -> {
          log.debug(entryNode.toString());
          final var inc = count.getAndIncrement();
          final var entry = entryToDebugChunk.apply(entryNode, names.get(inc));
          return Optional.of(entry);
        })
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  private Flux<JsonNode> parseHar(final Path harLocalPath) {
    return Flux.create(sink -> {
      try (InputStream inputStream = Files.newInputStream(harLocalPath);
           JsonParser parser = mapper.getFactory().createParser(inputStream);
      ) {
        var inEntries = false;
        while (parser.nextToken() != null) {
          final String currentName = parser.getCurrentName();
          switch (parser.getCurrentToken()) {
            case START_OBJECT:
              if (inEntries) {
                final JsonNode entryNode = parser.readValueAs(JsonNode.class);
                sink.next(entryNode);
              }
              break;
            case START_ARRAY:
              if (ENTRIES.equals(currentName)) {
                inEntries = true;
              }
              break;

            case END_ARRAY:
              if (ENTRIES.equals(currentName)) {
                inEntries = false;
              }
              break;

            default:
              // Nothing to do
              break;
          }
        }
      } catch (Exception e) {
        log.error("Failed to parse HAR", e);
        sink.error(e);
      }
      sink.complete();
    });
  }
}
