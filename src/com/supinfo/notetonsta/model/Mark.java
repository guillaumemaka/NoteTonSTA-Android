package com.supinfo.notetonsta.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The persistent class for the marks database table.
 * 
 */

public class Mark implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private String key;
	
	@JsonProperty
	private Double slideMark;
	
	@JsonProperty
	private Double speakerMark;
	
	@JsonProperty
	private String idbooster;
	
	@JsonProperty
	private String coment;
	
	@JsonProperty
	private String intervention;

	public Mark() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getSlideMark() {
		return slideMark;
	}

	public void setSlideMark(Double slideMark) {
		this.slideMark = slideMark;
	}

	public Double getSpeakerMark() {
		return speakerMark;
	}

	public void setSpeakerMark(Double speakerMark) {
		this.speakerMark = speakerMark;
	}

	public String getIdbooster() {
		return idbooster;
	}

	public void setIdbooster(String idbooster) {
		this.idbooster = idbooster;
	}

	public String getComent() {
		return coment;
	}

	public void setComent(String coment) {
		this.coment = coment;
	}
		
	public void setIntervention(String keyString){
		this.intervention = keyString;				
	}
	
	public String getIntervention() {
		return intervention;
	}
}