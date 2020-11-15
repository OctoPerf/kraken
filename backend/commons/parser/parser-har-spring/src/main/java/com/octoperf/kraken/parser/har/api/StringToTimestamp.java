package com.octoperf.kraken.parser.har.api;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.ToLongFunction;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
final class StringToTimestamp implements ToLongFunction<String> {

  SimpleDateFormat format;

  @Autowired
  public StringToTimestamp() {
    this.format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public long applyAsLong(String dateStr) {
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
