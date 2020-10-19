package com.goldenrealstate.todo.data.client;

public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1762196437438739628L;

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
