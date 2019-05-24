package com.hilats.elections2019.server;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class Server {

    // Default deployment URI
    public static final String BASE_URL = "http://0.0.0.0:8080";

    public static final String DATA_PATH = "./data";

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
    }

    static HttpServer startServer(URI baseUri, ResourceConfig application) {
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, application);
    }

    /**
     * Main entry point to start server.
     * Supported system properties :
     *   <li>baseurl : URL to listen to. Defaults to http://0.0.0.0:8080</li>
     *   <li>datapath : where tolook for elections data files. Defaults to './data'</li>
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String baseUrl = BASE_URL;
        if (System.getProperties().containsKey("baseurl")) {
            baseUrl = System.getProperties().getProperty("baseurl");
        }

        String dataPath = DATA_PATH;
        if (System.getProperties().containsKey("datapath")) {
            dataPath = System.getProperties().getProperty("datapath");
        }

        System.out.println("Starting grizzly...");
        HttpServer httpServer = startServer(
                URI.create(baseUrl),
                new ElectionsApplication(new File(dataPath).toPath()));

        /*
        // register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("Stopping server..");
            serverInstance.stop();
        }, "shutdownHook"));

        Thread.currentThread().join();
        */

        System.in.read();
        httpServer.shutdownNow();
    }

}
