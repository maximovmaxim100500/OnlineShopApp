-- liquibase formatted sql

-- changeset Denis Siliukov:1
CREATE TABLE users
(
    id serial NOT NULL,
    email character varying(32) NOT NULL,
    password character varying(255) NOT NULL,
    first_name character varying(16) NOT NULL,
    last_name character varying(16) NOT NULL,
    phone character varying(25) NOT NULL,
    role character varying(25) NOT NULL,
    image character varying(255),

    CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- changeset Denis Siliukov:2
CREATE TABLE avatars
(
    id serial NOT NULL,
    file_size bigint NOT NULL,
    media_type character varying(255) NOT NULL,
    data oid NOT NULL,
    user_id integer,

    CONSTRAINT avatars_pkey PRIMARY KEY (id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES avatars (id)
);

-- changeset Denis Siliukov:3
CREATE INDEX email_idx
    ON users (email)
;

-- changeset Denis SIliukov:4
CREATE TABLE images
(
    id serial NOT NULL,
    file_size bigint,
    media_type character varying(255),
    path character varying(255),
    ad_id integer,

    CONSTRAINT images_pkey PRIMARY KEY (id),
    CONSTRAINT ad_id_unique UNIQUE (ad_id)
);

-- changeset Denis Siliukov:5
CREATE TABLE ads
(
    pk serial NOT NULL,
    description character varying(64) NOT NULL,
    price integer NOT NULL,
    title character varying(32) NOT NULL,
    user_id integer NOT NULL,

    CONSTRAINT ads_pkey PRIMARY KEY (pk),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

-- changeset Denis Siliukov:6
ALTER TABLE images
    ADD CONSTRAINT ad_pk_fkey FOREIGN KEY (ad_id)
        REFERENCES ads (pk)
        ON DELETE CASCADE
;

-- changeset Denis Siliukov:7
CREATE INDEX ad_id_idx
    ON images (ad_id)
;

-- changeset Denis Siliukov:8
CREATE TABLE comments
(
    pk serial NOT NULL,
    created_at timestamp without time zone NOT NULL,
    text character varying(64) NOT NULL,
    ad_id integer NOT NULL,
    user_id integer NOT NULL,

    CONSTRAINT comments_pkey PRIMARY KEY (pk),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT ad_id_fkey FOREIGN KEY (ad_id)
        REFERENCES ads (pk)
        ON DELETE CASCADE
);