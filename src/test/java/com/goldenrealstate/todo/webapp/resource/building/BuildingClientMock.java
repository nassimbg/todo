package com.goldenrealstate.todo.webapp.resource.building;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.building.Building;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BuildingClientMock implements BuildingClient {

  private final Map<String, Building> cache;
  private final Random random;

  public BuildingClientMock() {
    cache = new HashMap<>();
    random = new Random(1);
  }

  @Override
  public String post(final Building ob) {
    final String id = String.valueOf(random.nextInt());
    ob.setId(id);
    cache.put(id, ob);
    return id;
  }

  @Override
  public Building get(final String id) throws NotFoundException {
    final Building building = cache.get(id);

    if (building == null) {
      throw new NotFoundException("Building with id " + id + " not found");
    }
    return building;
  }

  @Override
  public Collection<Building> getAll() {
    return cache.values();
  }
}
