CREATE TABLE articles (
	aid INTEGER AUTOINCREMENT,
	date DATETIME,
	PRIMARY KEY (aid)
);

CREATE TABLE workers (
	wid INTEGER AUTOINCREMENT,
	rating FLOAT,
	PRIMARY KEY (wid)
);

CREATE TABLE tasks (
	tid INTEGER AUTOINCREMENT,
	description VARCHAR(255),
	callbacklink VARCHAR(255),
	price FLOAT,
	aid INTEGER,
	PRIMARY KEY (tid),
	FOREIGN KEY (aid) REFERENCES articles(aid)
);

CREATE TABLE tags (
	name VARCHAR(255),
	tid INTEGER,
	PRIMARY KEY (name),
	FOREIGN KEY (tid) REFERENCES tasks(tid)
);

CREATE TABLE ratings (
	name VARCHAR(255),
	rid INTEGER,
	value FLOAT,
	tid INTEGER,
	PRIMARY KEY (name, rid),
	FOREIGN KEY (name) REFERENCES tags(name),
	FOREIGN KEY (tid) REFERENCES tasks(tid)
);

CREATE TABLE tasks_taken_by (
	tid INTEGER,
	wid INTEGER,
	PRIMARY KEY (tid, wid),
	FOREIGN KEY (tid) REFERENCES tasks(tid),
	FOREIGN KEY (wid) REFERENCES workers(wid)
);

CREATE TABLE (
);