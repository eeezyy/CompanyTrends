package aic13.group6.topic2.entities;

public enum State {
	CREATED,		// Job created in DB
	PREPARED,		// Done fetching articles/data (crawler)
	ASSIGNED,		// Assigned to mock
	FINISHED,		// All task finished by mock
	ABORTED			// Aborted by user
}
