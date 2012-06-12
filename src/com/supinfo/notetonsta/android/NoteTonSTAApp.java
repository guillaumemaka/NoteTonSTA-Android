package com.supinfo.notetonsta.android;

import android.app.Application;
import android.content.SharedPreferences;

import com.supinfo.notetonsta.api.NoteTonSTA;

public class NoteTonSTAApp extends Application {
	public static final String PREFS_FILE = "NoteTonSTA_Prefs";
	private NoteTonSTA api;
	private SharedPreferences settings;
	
	@Override
	public void onCreate() {		
		super.onCreate();
		settings = getSharedPreferences(PREFS_FILE, 0);
		api = new NoteTonSTA();
	}

	public NoteTonSTA getApi() {
		return api;
	}

	public void setApi(NoteTonSTA api) {
		this.api = api;
	}

	public SharedPreferences getSettings() {
		return settings;
	}

	public void setSettings(SharedPreferences settings) {
		this.settings = settings;
	}	
}
