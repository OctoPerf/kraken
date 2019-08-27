package com.kraken.analysis.server;

import com.kraken.analysis.entity.HttpHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class HeadersToExtension implements Function<List<HttpHeader>, String> {

  private static final String CONTENT_TYPE = "Content-Type";

  @Override
  public String apply(List<HttpHeader> headers) {
    final var contentType = headers.stream().filter(httpHeader -> CONTENT_TYPE.equalsIgnoreCase(httpHeader.getKey())).findFirst();
    return contentType.map(httpHeader -> {
      try {
        final var mimeType = MimeTypes.getDefaultMimeTypes().forName(httpHeader.getValue().split(";")[0]);
        return ofNullable(emptyToNull(mimeType.getExtension())).orElse(".txt");
      } catch (MimeTypeException e) {
        log.error("Failed to parse mime type", e);
      }
      return ".txt";
    }).orElse(".txt");
  }

}
