package com.kraken.har.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
final class StringToTimestamp implements Function<String, Long> {

  SimpleDateFormat format;

  @Autowired
  public StringToTimestamp() {
    this.format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public Long apply(String dateStr) {
    Date date;
    try {
      date = this.format.parse(dateStr);
    } catch (ParseException e) {
      log.error("Failed to parse date " + dateStr, e);
      date = new Date();
    }
    return date.getTime();
  }

}
