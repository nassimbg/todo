package com.goldenrealstate.todo.webapp.resource.person;

import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.PersonClient;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.person.Person;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static com.goldenrealstate.todo.webapp.util.PathBuilder.PERSONS;

@Path(PERSONS)
public final class PersonResource {
  private static final String PERSON_ID = "personId";

  private final PersonClient personClient;

  public PersonResource(@Context ClientFactory clientFactory) {
    this.personClient = clientFactory.getPersonClient();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Id create(Person person) {
    return Id.fromString(personClient.post(person));
  }

  @Path("{" + PERSON_ID + "}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Person getPerson(@PathParam(PERSON_ID) String id) {
    return personClient.get(id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Person> getPersons() {
    return personClient.getAll();
  }
}
