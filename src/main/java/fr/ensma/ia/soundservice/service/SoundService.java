package fr.ensma.ia.soundservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.ensma.a3.ia.be.dao.TagAnnoDAO;
import fr.ensma.ia.soundservice.to.Annotation;
import fr.ensma.ia.soundservice.to.Sound;
import fr.ensma.ia.soundservice.to.Tag;

@Path("/")
public class SoundService {
    
    @POST
    @Path("/annotation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAnnotation(Annotation a) {
    	// TODO: add the annotation to the database
    	
    	
    	
    	
    	TagAnnoDAO tanndao = new TagAnnoDAO();
    	
    	
    	System.out.println("Nouvelle annotation de la part de " + a.getAuthor());
        return Response.ok().build();
    }
    
    @GET
    @Path("/sounds")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sound> getSounds() {
    	// TODO: retrieves a list of sounds from the database
    	List<Sound> sounds = new ArrayList<Sound>();
    	Sound s = new Sound("son0.wav", "./sound/son0.wav");
    	List<Tag> tags = new ArrayList<Tag>();
    	tags.add(new Tag("Turbine airplane"));
    	tags.add(new Tag("Propeller airplane"));
    	tags.add(new Tag("Airplane"));
    	s.setTags(tags);
    	sounds.add(s);
    	sounds.add(new Sound("son1.wav", "./sound/son1.wav"));
    	sounds.add(new Sound("son2.wav", "./sound/son2.wav"));
    	return sounds;
    }
}
