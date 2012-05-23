package com.supinfo.notetonsta.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The persistent class for the interventions database table.
 * 
 */
public class Intervention implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String key;

	@JsonProperty
	private Date startDate;

	@JsonProperty
	private Date endDate;

	@JsonProperty
	private String subject;

	@JsonProperty
	private String description;
	
	@JsonProperty
	private String status;
	
	@JsonProperty
	private int countMark;

	public Intervention() {

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCountMark() {
		return countMark;
	}

	public void setCountMark(int countMark) {
		this.countMark = countMark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}