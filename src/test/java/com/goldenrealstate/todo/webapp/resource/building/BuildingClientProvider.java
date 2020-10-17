package com.goldenrealstate.todo.webapp.resource.building;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.webapp.models.building.Building;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import javax.ws.rs.ext.Provider;

@Provider
public class BuildingClientProvider implements Supplier<BuildingClient> {

  @Override
  public BuildingClient get() {
    return new BuildingClientMock();
  }

  private static final class BuildingClientMock implements BuildingClient {

    private final Map<String, Building> cache;
    private final Random random;

    private BuildingClientMock() {
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
  }
}
