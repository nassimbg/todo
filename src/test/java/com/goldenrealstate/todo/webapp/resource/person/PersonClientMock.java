package com.goldenrealstate.todo.webapp.resource.person;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.PersonClient;
import com.goldenrealstate.todo.webapp.models.person.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class PersonClientMock implements PersonClient {

  private final Map<String, Person> cache;
  private final Random random;

  public PersonClientMock() {
    cache = new HashMap<>();
    random = new Random(1);
  }

  @Override
  public String post(final Person ob) {
    final String id = String.valueOf(random.nextInt());
    ob.setId(id);
    cache.put(id, ob);
    return id;
  }

  @Override
  public Person get(final String id) throws NotFoundException {
    final Person building = cache.get(id);

    if (building == null) {
      throw new NotFoundException("Person with id " + id + " not found");
    }
    return building;
  }

  @Override
  public Collection<Person> getAll() {
    return cache.values();
  }
}
