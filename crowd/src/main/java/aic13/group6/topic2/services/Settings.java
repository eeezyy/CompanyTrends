package aic13.group6.topic2.services;

import java.util.prefs.Preferences;

public class Settings {
	
	// default values
	private final static String BASE_URL = "http://localhost:8080/";
	private final static String CROWD_BASE_API = "crowd/rest/";
	private final static String CALLBACK_RESOURCE = "job/callback";
	private final static String MOCK_BASE_API = "mock/rest/";
	private final static String TASK_RESOURCE = "task";
	private final static String DESCRIPTION = "Please read the article and rate the following topic with the options below: ";
	
	private final static int WORKER_PER_TASK = 5;
	// TODO
	//private final static Map ANSWER_OPTIONS = ...;
	
	// key
	private final static String BASE_URL_KEY = "base-url";
	private final static String CROWD_BASE_API_KEY = "crowd-base-api";
	private final static String CALLBACK_RESOURCE_KEY = "callback-resource";
	private final static String MOCK_BASE_API_KEY = "mock-base-api";
	private final static String TASK_RESOURCE_KEY = "task-resource";
	private final static String DESCRIPTION_KEY = "description";
	
	private final static String WORKER_PER_TASK_KEY = "workers-per-task";
	//private final static String ANSWER_OPTIONS_KEY = "answer-options";
	
	
	private static Preferences prefs = Preferences.userNodeForPackage(Settings.class);
	
	public static void setBaseUrl(String text) {
		prefs.put(BASE_URL_KEY, text);
	}
	
	public static String getBaseUrl() {
		return prefs.get(BASE_URL_KEY, BASE_URL);
	}
	
	public static void setCrowdBaseAPI(String text) {
		prefs.put(CROWD_BASE_API_KEY, text);
	}
	
	public static String getCrowdBaseAPI() {
		return prefs.get(CROWD_BASE_API_KEY, CROWD_BASE_API);
	}
	
	public static void setCallbackResource(String text) {
		prefs.put(CALLBACK_RESOURCE_KEY, text);
	}
	
	public static String getCallbackResource() {
		return prefs.get(CALLBACK_RESOURCE_KEY, CALLBACK_RESOURCE);
	}
	
	public static void setMockBaseAPI(String text) {
		prefs.put(MOCK_BASE_API_KEY, text);
	}
	
	public static String getMockBaseAPI() {
		return prefs.get(MOCK_BASE_API_KEY, MOCK_BASE_API);
	}
	
	public static void setTaskResource(String text) {
		prefs.put(TASK_RESOURCE_KEY, text);
	}
	
	public static String getTaskResource() {
		return prefs.get(TASK_RESOURCE_KEY, TASK_RESOURCE);
	}
	
	public static void setDescription(String text) {
		prefs.put(DESCRIPTION_KEY, text);
	}
	
	public static String getDescription() {
		return prefs.get(DESCRIPTION_KEY, DESCRIPTION);
	}
	
	public static void setWorkerPerTask(int value) {
		prefs.putInt(WORKER_PER_TASK_KEY, value);
	}
	
	public static int getWorkerPerTask() {
		return prefs.getInt(WORKER_PER_TASK_KEY, WORKER_PER_TASK);
	}

}
