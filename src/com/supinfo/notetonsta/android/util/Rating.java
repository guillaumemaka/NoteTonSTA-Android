package com.supinfo.notetonsta.android.util;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class Rating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private double avgSpeaker;
	
	@JsonProperty
	private double avgSlide;
	
	@JsonProperty
	private double globalAvg;

	public Rating() {
		super();
	}

	public double getAvgSpeaker() {
		return avgSpeaker;
	}

	public void setAvgSpeaker(double avgSpeaker) {
		this.avgSpeaker = avgSpeaker;
	}

	public double getAvgSlide() {
		return avgSlide;
	}

	public void setAvgSlide(Double avgSlide) {
		this.avgSlide = avgSlide;
	}

	public double getGlobalAvg() {
		return globalAvg;
	}

	public void setGlobalAvg(double globalAvg) {
		this.globalAvg = globalAvg;
	}

}
