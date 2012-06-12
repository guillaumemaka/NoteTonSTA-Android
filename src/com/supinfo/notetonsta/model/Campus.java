package com.supinfo.notetonsta.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The persistent class for the campus datastore table.
 * 
 */

public class Campus implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String key;

	@JsonProperty
	private String name;

	public Campus() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}