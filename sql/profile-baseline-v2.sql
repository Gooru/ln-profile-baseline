-- These tables are going to be owned by datascope
-- Migration may be needed from existing table to the new details table

CREATE TABLE baseline_learner_profile_master (
    id bigserial NOT NULL PRIMARY KEY,
    tx_subject_code text NOT NULL,
    class_id text,
    course_id text NOT NULL,
    user_id text NOT NULL,
    lp_data text NOT NULL,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL
);

CREATE UNIQUE INDEX blpm_ucsc_unique_idx ON baseline_learner_profile_master USING btree (user_id, course_id, tx_subject_code, class_id) WHERE (class_id IS NOT NULL);

CREATE UNIQUE INDEX blpm_ucs_unique_idx ON baseline_learner_profile_master USING btree (user_id, course_id, tx_subject_code) WHERE (class_id IS NULL);

CREATE TABLE baseline_learner_profile_details (
    id bigserial NOT NULL PRIMARY KEY,
    blpm_id bigint NOT NULL REFERENCES baseline_learner_profile_master(id) ON DELETE CASCADE,
    gut_code text NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT blpd_status_check CHECK ((status = ANY (ARRAY[0, 1, 2, 3, 4, 5])))
);

CREATE UNIQUE INDEX blpd_blpmidgut_unq ON baseline_learner_profile_details USING btree (blpm_id, gut_code);


