CREATE TABLE articles (
	url TEXT PRIMARY KEY,
	date DATETIME,
	title TEXT,
	text TEXT,
	usable BOOLEAN
);

CREATE TABLE tags (
	name TEXT PRIMARY KEY
);

CREATE TABLE workers (
	wid INTEGER PRIMARY KEY AUTOINCREMENT,
	rating FLOAT
);

CREATE TABLE tasks (
	tid INTEGER PRIMARY KEY AUTOINCREMENT,
	description TEXT,
	callbacklink TEXT,
	price FLOAT,
	url TEXT,
	wid INTEGER,
	FOREIGN KEY (wid) REFERENCES workers(wid),
	FOREIGN KEY (url) REFERENCES articles(url)
);

CREATE TABLE ratings (
	rid INTEGER PRIMARY KEY AUTOINCREMENT,
	value FLOAT,
	tid INTEGER,
	tag TEXT,
	FOREIGN KEY (tid) REFERENCES tasks(tid),
	FOREIGN KEY (tag) REFERENCES tags(name)	
);

CREATE TABLE has_tags (
	url TEXT,
	tag TEXT,
	PRIMARY KEY (url, tag),
	FOREIGN KEY (url) REFERENCES articles(url),
	FOREIGN KEY (tag) REFERENCES tags(name)
);

CREATE TABLE belong_to (
	tid INTEGER,
	tag TEXT,
	PRIMARY KEY (tid, tag),
	FOREIGN KEY (tid) REFERENCES tasks(tid),
	FOREIGN KEY (tag) REFERENCES tags(name)
);

