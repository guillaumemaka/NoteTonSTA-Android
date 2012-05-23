package com.supinfo.notetonsta.api;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.supinfo.notetonsta.android.util.Rating;
import com.supinfo.notetonsta.model.Campus;
import com.supinfo.notetonsta.model.Intervention;
import com.supinfo.notetonsta.model.Mark;

public class NoteTonSTA {
	static public final int REQUEST_CODE_EVALUATION = 0;
	static public final int RESULT_CODE_EVALUATION = 0;
	static public final int RESULT_CODE_BACK = -1;
	
	static private String jsonRepresentation;
	private String RootApi = "http://notetonsta-134508.appspot.com/rest/";
	private ClientResource client;
	private ObjectMapper mapper;
	private Boolean hasError;
	private MediaType mediaType;
	private String error;

	public NoteTonSTA() {
		this.hasError = false;
		this.mapper = new ObjectMapper();
		this.registerEngineConverters();
		this.mediaType = MediaType.APPLICATION_JSON;
		System.setProperty("java.net.preferIPv6Addresses", "false");
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Boolean hasError() {
		return hasError;
	}

	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}

	public void registerEngineConverters() {
		Engine.getInstance().getRegisteredConverters().clear();
		Engine.getInstance().getRegisteredConverters()
				.add(new JacksonConverter());
	}

	/*
	 * 
	 * 
	 * Intervention common operation
	 */

	public Intervention findIntervention(String interventionKey) {
		this.client = new ClientResource(String.format("%s%s%s%s",
				this.RootApi, ResourceLocator.InterventionResource,
				Action.FIND, interventionKey));

		Intervention intervention = null;
		String jsonResponse;
		try {
			jsonResponse = this.client.get(this.mediaType).getText();
			intervention = mapper.readValue(jsonResponse,
					RegisteredTypeReference.Intervention);
			NoteTonSTA.jsonRepresentation = jsonResponse;
		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return intervention;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return intervention;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return intervention;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return intervention;
		}

		return intervention;
	}

	public List<Intervention> getInterventionsForSpeaker(String speakerKey) {
		List<Intervention> interventions = null;
		this.client = new ClientResource(String.format("%s%s%s%s",
				this.RootApi, ResourceLocator.InterventionResource,
				Action.FIND_INTERVENTION_BY_SPEAKER, speakerKey));
		String jsonResponse;
		try {
			jsonResponse = this.client.get(this.mediaType).getText();
			interventions = mapper.readValue(jsonResponse,
					RegisteredTypeReference.InterventionList);
			NoteTonSTA.jsonRepresentation = jsonResponse;
		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		}

		return interventions;
	}

	public List<Intervention> getInterventionsForCampus(String campusKey) {
		List<Intervention> interventions = null;
		this.client = new ClientResource(String.format("%s%s%s%s",
				this.RootApi, ResourceLocator.InterventionResource,
				Action.FIND_INTERVENTION_BY_CAMPUS, campusKey));
		String jsonResponse;
		try {
			jsonResponse = this.client.get(this.mediaType).getText();
			interventions = mapper.readValue(jsonResponse,
					RegisteredTypeReference.InterventionList);
			NoteTonSTA.jsonRepresentation = jsonResponse;
		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return interventions;
		}

		return interventions;
	}

	public Boolean evaluate(Mark mark) {
		this.client = new ClientResource(String.format("%s%s%s", this.RootApi,
				ResourceLocator.InterventionResource, Action.EVALUATE));
		
		// Prevent chunked paquet
		this.client.setRequestEntityBuffering(true);
		
		String json_response;

		try {
			json_response = this.client.post(mark).getText();
			Status status = mapper.readValue(json_response, Status.class);

			switch (status) {
			case OK:
				return true;
			case CREATION_FAIL:
				setHasError(true);
				setError("Evaluation Failed");
				return false;
			}

		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return false;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return false;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return false;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return false;
		}

		return true;
	}

	public Rating getRatingForIntervention(Intervention intervention) {
		this.client = new ClientResource(String.format("%s%s%s%s",
				this.RootApi, ResourceLocator.MarkResource, Action.RATING,
				intervention.getKey()));

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse;

		Rating rating = new Rating();

		try {
			jsonResponse = client.get(mediaType).getText();
			
			rating = mapper.readValue(jsonResponse,
					RegisteredTypeReference.InterventionRating);
			NoteTonSTA.jsonRepresentation = jsonResponse;
		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return rating;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return rating;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return rating;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return rating;
		}

		return rating;
	}

	/*
	 * 
	 * 
	 * Campus common operations
	 */

	public List<Campus> getAllCampus() {
		List<Campus> campuses = null;
		this.client = new ClientResource(String.format("%s%s%s", this.RootApi,
				ResourceLocator.CampusResource, Action.FINDALL));
		String jsonResponse;

		try {
			jsonResponse = this.client.get(this.mediaType).getText();
			campuses = mapper.readValue(jsonResponse,
					RegisteredTypeReference.CampusList);
			NoteTonSTA.jsonRepresentation = jsonResponse;
			return campuses;
		} catch (JsonParseException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return campuses;
		} catch (JsonMappingException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return campuses;
		} catch (ResourceException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return campuses;
		} catch (IOException e) {
			this.setHasError(true);
			this.setError(e.getMessage());
			return campuses;
		}
	}

	
	/*
	 * 	Retrieve the last jsonResponse string representation
	 * 
	 */
	static public String getJsonRepresentation() {
		return jsonRepresentation;
	}

}