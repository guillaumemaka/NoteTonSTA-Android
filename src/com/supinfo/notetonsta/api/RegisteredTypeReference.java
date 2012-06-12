package com.supinfo.notetonsta.api;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import com.supinfo.notetonsta.android.util.Rating;
import com.supinfo.notetonsta.model.Campus;
import com.supinfo.notetonsta.model.Intervention;
import com.supinfo.notetonsta.model.Mark;
import com.supinfo.notetonsta.model.Speaker;

class RegisteredTypeReference {
	public static final TypeReference<Intervention> Intervention = new TypeReference<Intervention>() {
	};
	public static final TypeReference<Campus> Campus = new TypeReference<Campus>() {
	};
	public static final TypeReference<Mark> Mark = new TypeReference<Mark>() {
	};
	public static final TypeReference<Speaker> Speaker = new TypeReference<Speaker>() {
	};
	public static final TypeReference<List<Intervention>> InterventionList = new TypeReference<List<Intervention>>() {
	};
	public static final TypeReference<List<Campus>> CampusList = new TypeReference<List<Campus>>() {
	};
	public static final TypeReference<List<Mark>> MarkList = new TypeReference<List<Mark>>() {
	};
	public static final TypeReference<Rating> InterventionRating = new TypeReference<Rating>() {
	};
}