ALTER TABLE profile_bootcamp
ADD COLUMN id BIGSERIAL;

ALTER TABLE profile_bootcamp
DROP CONSTRAINT profile_bootcamp_pkey;

ALTER TABLE profile_bootcamp
ADD CONSTRAINT profile_bootcamp_pkey PRIMARY KEY (id);

ALTER TABLE profile_bootcamp
ADD CONSTRAINT unique_profile_bootcamp UNIQUE (bootcamp_id, profile_id);

-- REVERT
-- ALTER TABLE profile_bootcamp DROP COLUMN id;
-- ALTER TABLE profile_bootcamp DROP CONSTRAINT profile_bootcamp_pkey;
-- ALTER TABLE profile_bootcamp ADD CONSTRAINT profile_bootcamp_pkey PRIMARY KEY (technology_id, profile_id);
-- ALTER TABLE profile_bootcamp DROP CONSTRAINT unique_technology_profile;
-- DELETE FROM flyway_schema_history WHERE version = '1.2';