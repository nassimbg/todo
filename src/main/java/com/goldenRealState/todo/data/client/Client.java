package com.goldenrealstate.todo.data.client;

public interface Client<T, V> {

  V post(T ob);

  T get(V id) throws NotFoundException;
}
