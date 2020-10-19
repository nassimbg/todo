package com.goldenrealstate.todo.webapp.resource.person;


import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.person.Person;
import com.goldenrealstate.todo.webapp.resource.AbstractJerseyTest;
import com.goldenrealstate.todo.webapp.resource.ClientFactoryProvider;
import com.goldenrealstate.todo.webapp.util.PathBuilder;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class PersonResourceTest extends AbstractJerseyTest {

  @Override
  protected ResourceConfig configure() {
    final ResourceConfig resourceConfig = super.configure();

    resourceConfig.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(ClientFactoryProvider.class).to(ClientFactory.class).in(Singleton.class);
      }
    });
    resourceConfig.register(PersonResource.class);
    return resourceConfig;
  }

  @Test
  public void testCreatePerson() {
    final Person person = new Person("person 1");

    final Response response = target(PathBuilder.PERSONS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals("-1155869325", response.readEntity(Id.class).getId());
  }

  @Test
  public void testGetForExistingPerson() {
    final Person person = new Person("person 1");

    final Response postResponse = target(PathBuilder.PERSONS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), postResponse.getStatus());

    final String id = "-1155869325";
    assertEquals(id, postResponse.readEntity(Id.class).getId());

    final Person retrievedPerson = target(PathBuilder.buildPath(PathBuilder.PERSONS, id))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Person.class);

    person.setId(id);
    assertEquals(person, retrievedPerson);
  }

  @Test
  public void testGetForNonExistingPerson() {
    final String id = "-1155869325";

    final Response response = target(PathBuilder.buildPath(PathBuilder.PERSONS, id))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertEquals("Person with id " + id + " not found", response.readEntity(String.class));
  }
}
