CREATE TABLE profile (
	id BIGSERIAL NOT NULL,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(90),
	PRIMARY KEY(id)
);

CREATE TABLE profile_bootcamp (
	profile_id BIGINT NOT NULL,
	bootcamp_id BIGINT NOT NULL,
	PRIMARY KEY(profile_id, bootcamp_id),
	FOREIGN KEY (profile_id) REFERENCES profile(id)
);