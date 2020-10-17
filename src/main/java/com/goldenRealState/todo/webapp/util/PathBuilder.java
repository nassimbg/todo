package com.goldenrealstate.todo.webapp.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PathBuilder {

  public static final String BUILDINGS = "buildings";

  private PathBuilder() {
    //do nothing
  }

  public static String buildPath(Object... pathParams) {
    return Stream.of(pathParams)
        .map(Object::toString)
        .collect(Collectors.joining("/"));
  }
}
