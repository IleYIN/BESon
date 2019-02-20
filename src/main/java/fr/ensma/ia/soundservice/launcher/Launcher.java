package fr.ensma.ia.soundservice.launcher;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import fr.ensma.ia.soundservice.service.SoundService;
import fr.ensma.ia.soundservice.service.util.ThrowableExceptionMapper;

public class Launcher {
    public static final String SERVER = "http://localhost/";
    public static final int PORT = 9991;
    public static final URI BASE_URI = UriBuilder.fromUri(SERVER).port(PORT).build();

    public static void killHolderOfPort(int port) {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "lsof -i :" + port + " -s TCP:LISTEN -t | xargs kill"});
            p.waitFor();
            Thread.sleep(50);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        killHolderOfPort(PORT);
        
        ResourceConfig rc = new ResourceConfig();
        rc.registerClasses(SoundService.class, ThrowableExceptionMapper.class);
        rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
            server.start();
            
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...",
                    BASE_URI));
            
            System.in.read();
            server.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
