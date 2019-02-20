package fr.ensma.ia.soundservice.to;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sound {
	private List<Tag> tags;
	@JsonProperty("id_sound")
	private String idSound;
	private String url;
	
	public Sound() {}
	public Sound(String idSound, String url) {
		this.idSound = idSound;
		this.url = url;
	}
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
