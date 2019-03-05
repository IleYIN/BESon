package fr.ensma.ia.soundservice.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.ia.soundservice.tao.ISoundDAO;
import fr.ensma.ia.soundservice.tao.ITagAnnoDAO;
import fr.ensma.ia.soundservice.tao.SoundDAO;
import fr.ensma.ia.soundservice.tao.TagAnnoDAO;
import fr.ensma.ia.soundservice.to.Annotation;
import fr.ensma.ia.soundservice.to.Sound;

@Path("/")
public class SoundService {
    
	private static final Logger logger = LogManager.getLogger(SoundService.class);
	
    @POST
    @Path("/annotation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAnnotation(Annotation a) {
    	//add the annotation to the database
    	ITagAnnoDAO tagAnnoDao = TagAnnoDAO.getInstance();
    	tagAnnoDao.addTagAnnotation(a);
    	
    	logger.info("Nouvelle annotation de la part de " + a.getAuthor());
        return Response.ok().build();
    }
    
    @GET
    @Path("/sounds")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sound> getSounds() {
    	//retrieves a list of sounds from the database
    	
//    	List<Sound> sounds = new ArrayList<Sound>();
//    	Sound s = new Sound("son0.wav", "./sound/son0.wav");
//    	List<Tag> tags = new ArrayList<Tag>();
//    	tags.add(new Tag("Turbine airplane"));
//    	tags.add(new Tag("Propeller airplane"));
//    	tags.add(new Tag("Airplane"));
//    	s.setTags(tags);
//    	sounds.add(s);
//    	sounds.add(new Sound("son1.wav", "./sound/son1.wav"));
//    	sounds.add(new Sound("son2.wav", "./sound/son2.wav"));
//    	return sounds;
    	ISoundDAO soundDao = SoundDAO.getInstance();
    	logger.info("Get sounds from the server");
    	return soundDao.getListSound();
    }
}
