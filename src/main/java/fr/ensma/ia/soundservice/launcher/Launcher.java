package fr.ensma.ia.soundservice.launcher;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import javax.ws.rs.core.UriBuilder;

import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.loaders.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import fr.ensma.ia.soundservice.service.SoundService;
import fr.ensma.ia.soundservice.service.util.ThrowableExceptionMapper;
import fr.ensma.ia.soundservice.util.ServerConfig;

public class Launcher {
//    public static final String SERVER = "http://localhost/";
	public static final String SERVER = "http://193.55.163.218/";

    public static final int PORT = 9991;
    public static final URI BASE_URI = UriBuilder.fromUri(SERVER).port(PORT).build();
    
    private static final Logger logger = LogManager.getLogger(Launcher.class);

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
        
        validateConfig();
        
        ResourceConfig rc = new ResourceConfig();
        rc.registerClasses(SoundService.class, ThrowableExceptionMapper.class);
        rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

        
        
        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
            server.start();
            
            logger.info(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\n",
                    BASE_URI));
            
//            System.in.read();
//            server.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void validateConfig() {
        final String CLASSPATH_PROTOCOL = "classpath:";

        Sources sources = ServerConfig.class.getAnnotation(Sources.class);
        for (String source : sources.value()) {
            Properties props = new Properties();
            URI uri = null;
            if (source.startsWith(CLASSPATH_PROTOCOL)) {
                String path = source.substring(CLASSPATH_PROTOCOL.length());
                try {
                    uri = ClassLoader.getSystemResource(path).toURI();
                } catch (URISyntaxException | NullPointerException e) {
                    logger.error("Could not find the configuration file " + source);
                    System.exit(-1);
                }
            } else {
                try {
                    uri = new URI(source);
                } catch (URISyntaxException e) {
                    logger.error("Could not find the configuration file " + source);
                    System.exit(-1);
                }
            }

            try {
                new PropertiesLoader().load(props, uri);
            } catch (IOException e) {
                logger.error("Could not read configuration file " + source);
                System.exit(-1);
            }

            logger.info("Listing configuration properties");
            for (Entry<Object, Object> e : props.entrySet()) {
                logger.info(e.getKey() + "=" + e.getValue());
            }

            for (Method m : ServerConfig.class.getMethods()) {
                Key key = m.getAnnotation(Key.class);
                if (!props.containsKey(key.value())) {
                    logger.error("Missing required configuration property " + key.value());
                    System.exit(-1);
                }
            }
        }
    }
	
}
