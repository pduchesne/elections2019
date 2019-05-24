package com.hilats.elections2019.server.rest.resources;

import com.hilats.elections2019.server.ElectionsApplication;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by pduchesne on 28/06/14.
 */
public class AbstractResource {

    @Context
    protected SecurityContext securityContext;

    @Context
    ElectionsApplication app;

    public ElectionsApplication getApplication() {
        return app;
    }

}
