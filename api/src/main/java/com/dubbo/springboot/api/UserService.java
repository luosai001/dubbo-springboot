package com.dubbo.springboot.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by sai.luo on 2018-6-27.
 */

@Path("/user")
public interface UserService {
    @Path("/register/{id:\\d+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    User getUser(@PathParam("id") Long id);
}
