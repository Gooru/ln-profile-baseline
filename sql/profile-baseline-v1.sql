-- drop table profile_baseline_queue

-- This queue table is going to be owned by Nucleus and is part of core DB unlike the result tables which are owned by datascope

create table profile_baseline_queue (
    id bigserial NOT NULL,
    user_id uuid NOT NULL,
    course_id uuid NOT NULL,
    class_id uuid,
    priority int check (priority::int = ANY(ARRAY[1::int, 2::int, 3::int, 4::int])),
    status int NOT NULL DEFAULT 0 CHECK (status::int = ANY (ARRAY[0::int, 1::int, 2::int])),
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT pfq_pkey PRIMARY KEY (id)
);

ALTER TABLE profile_baseline_queue OWNER TO nucleus;

CREATE UNIQUE INDEX pbq_ucc_unq_idx
    ON profile_baseline_queue (user_id, course_id, class_id)
    where class_id is not null;

CREATE UNIQUE INDEX pbq_ucc_null_unq_idx
    ON profile_baseline_queue (user_id, course_id)
    where class_id is null;

COMMENT on TABLE profile_baseline_queue IS 'Persistent queue for profile baseline tasks';
COMMENT on COLUMN profile_baseline_queue.status IS '0 means queued, 1 means dispatched for processing, 2 means in process';
COMMENT on COLUMN profile_baseline_queue.priority IS '1 means setting changed in class, 2 means course assigned to class, 3 means users joining class and 4 means OOB request for user accessing the rescoped content';

-- Alter on class table

alter table class add column grade_lower_bound bigint;
alter table class add column grade_upper_bound bigint;
alter table class add column grade_current bigint;


