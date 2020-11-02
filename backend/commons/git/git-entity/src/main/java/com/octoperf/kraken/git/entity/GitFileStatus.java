package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * changed :
 * <pre>
 * 1 <XY> <sub> <mH> <mI> <mW> <hH> <hI> <path>
 * </pre>
 * <p>
 * unmerged :
 * <pre>
 * u <xy> <sub> <m1> <m2> <m3> <mW> <h1> <h2> <h3> <path>
 * </pre>
 */
@Value
public class GitFileStatus {
  // <XY> A 2 character field containing the staged and unstaged XY values described in the short format, with unchanged indicated by a "." rather than a space.
  String xy;
  // <path> The pathname
  String path;

  @JsonCreator
  @Builder(toBuilder = true)
  public GitFileStatus(@NonNull @JsonProperty("xy") final String xy,
                       @NonNull @JsonProperty("path") final String path) {
    this.xy = xy;
    this.path = path;
  }
}
