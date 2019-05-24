package com.hilats.elections2019.server.rest.resources;

import com.hilats.elections2019.storage.FileStorage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class MainResource extends AbstractResource
{
    @GET
    @Path("/")
    public Object getTypes() {
        return FileStorage.ELECTION_TYPE.values();
    }

    @GET
    @Path("/{type}")
    public Object getLevels(@PathParam("type") String type) {
        return Response.ok(FileStorage.POLLING_LEVEL.values()).build();
    }

    @GET
    @Path("/{type}/{level}")
    public Object get(@PathParam("type") String type,
                      @PathParam("level") String level) {
        return getApplication().getStorage().getInsCodes(
                FileStorage.ELECTION_TYPE.valueOf(type.toUpperCase()),
                FileStorage.POLLING_LEVEL.valueOf(level));
    }

    @GET
    @Path("/{type}/{level}/{ins}")
    public Object get(@PathParam("type") String type,
                      @PathParam("level") String level,
                      @PathParam("ins") String insCode) {
        return getApplication().getStorage().getResults(
                FileStorage.ELECTION_TYPE.valueOf(type.toUpperCase()),
                FileStorage.POLLING_LEVEL.valueOf(level),
                insCode);
    }

}
