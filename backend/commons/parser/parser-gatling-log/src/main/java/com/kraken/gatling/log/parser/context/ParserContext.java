package com.kraken.gatling.log.parser.context;

import com.kraken.analysis.entity.DebugEntry;

import java.util.Optional;

public interface ParserContext {

  LogParserState getState();

  void setState(LogParserState state);

  LogParserSubState getSubState();

  void setSubState(LogParserSubState state);

  DebugEntry.DebugEntryBuilder getChunkBuilder();

  Optional<DebugEntry> delimiterEnd();

  void setRequestNameStatus(String line);

  void appendRequestHeader(String line);

  void appendRequestCookie(String line);

  void appendResponseHeader(String line);

  void appendRequestBody(String line);

  void appendResponseBody(String line);

  void reset();

}
