package com.hilats.elections2019.server;

import com.hilats.elections2019.storage.FileStorage;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class ElectionsApplication extends ResourceConfig {

    private FileStorage storage;

    public ElectionsApplication(Path dataPath) {


        property(ServerProperties.MEDIA_TYPE_MAPPINGS, "rdf: application/rdf+xml, txt : text/plain, xml : application/xml, json : application/json, jsonld : application/ld+json, html : text/html, ttl : text/turtle");

        // make sure status>=400 is indeed processed by our exceptionMapper
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);

        this.packages(ElectionsApplication.class.getPackage().getName()+".rest.resources");
        //this.register(ExceptionHandler.class);

        register(JacksonFeature.class);

        storage = new FileStorage(dataPath);
    }


    public FileStorage getStorage() {
        return storage;
    }
}
