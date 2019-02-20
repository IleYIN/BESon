package fr.ensma.ia.soundservice.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {
	@JsonProperty("id_tag")
	private String name;
	
	public Tag() {}
	
	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
	
		return this.name;
	}
}
