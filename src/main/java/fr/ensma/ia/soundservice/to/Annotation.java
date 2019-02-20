package fr.ensma.ia.soundservice.to;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Annotation {
	
	private List<Tag> tags;
	
	@JsonProperty("id_sound")
	private String idSound;
	
	private String author;
	
	
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public String getIdSound() {
		return idSound;
	}
	public void setIdSound(String idSound) {
		this.idSound = idSound;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
    
}
