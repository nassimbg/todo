package com.goldenrealstate.todo.data.client;

import java.util.Collection;

public interface Client<T, V> {

  V post(T ob);

  T get(V id) throws NotFoundException;

  Collection<T> getAll();
}
