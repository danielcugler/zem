--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.8
-- Dumped by pg_dump version 9.5.4

-- Started on 2017-02-03 09:07:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1 (class 3079 OID 11861)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2304 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 173 (class 1259 OID 24877)
-- Name: additional_information_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE additional_information_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 174 (class 1259 OID 24879)
-- Name: additional_information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE additional_information (
    additional_information_id integer DEFAULT nextval('additional_information_id_seq'::regclass) NOT NULL,
    information character varying(4000) NOT NULL
);


ALTER TABLE additional_information OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 24886)
-- Name: address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE address_id_seq OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 24888)
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address (
    address_id integer DEFAULT nextval('address_id_seq'::regclass) NOT NULL,
    street_name character varying(100),
    address_number integer,
    geografical_coordinates character varying,
    complement character varying(20),
    neighborhood_id integer DEFAULT 34236 NOT NULL
);


ALTER TABLE address OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 24895)
-- Name: addressc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE addressc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE addressc_id_seq OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 24897)
-- Name: attendance_time; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE attendance_time (
    entity_id integer NOT NULL,
    high_priority_time integer NOT NULL,
    medium_priority_time integer NOT NULL,
    low_priority_time integer NOT NULL,
    enabled integer NOT NULL
);


ALTER TABLE attendance_time OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 24905)
-- Name: broadcast_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE broadcast_message (
    broadcast_message_id integer NOT NULL,
    subject character varying(100) NOT NULL,
    message_body character varying(4000) NOT NULL,
    broadcast_message_category_id integer NOT NULL,
    registration_date timestamp without time zone NOT NULL,
    publication_date timestamp without time zone,
    created_by character varying(20) NOT NULL,
    creation_date timestamp without time zone NOT NULL,
    enabled integer DEFAULT 0,
    expiration_date timestamp without time zone,
    days_expiration integer
);


ALTER TABLE broadcast_message OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 24912)
-- Name: broadcast_message_broadcast_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE broadcast_message_broadcast_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE broadcast_message_broadcast_message_id_seq OWNER TO postgres;

--
-- TOC entry 2305 (class 0 OID 0)
-- Dependencies: 181
-- Name: broadcast_message_broadcast_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE broadcast_message_broadcast_message_id_seq OWNED BY broadcast_message.broadcast_message_id;


--
-- TOC entry 182 (class 1259 OID 24914)
-- Name: broadcast_message_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE broadcast_message_category (
    broadcast_message_category_id integer NOT NULL,
    name integer
);


ALTER TABLE broadcast_message_category OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 24917)
-- Name: broadcast_message_category_broadcast_message_category_id_seq_1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE broadcast_message_category_broadcast_message_category_id_seq_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE broadcast_message_category_broadcast_message_category_id_seq_1 OWNER TO postgres;

--
-- TOC entry 2306 (class 0 OID 0)
-- Dependencies: 183
-- Name: broadcast_message_category_broadcast_message_category_id_seq_1; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE broadcast_message_category_broadcast_message_category_id_seq_1 OWNED BY broadcast_message_category.broadcast_message_category_id;


--
-- TOC entry 184 (class 1259 OID 24919)
-- Name: call_classification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE call_classification (
    call_classification_id integer NOT NULL,
    name character varying(40) NOT NULL,
    address_required boolean DEFAULT false NOT NULL,
    enabled integer DEFAULT 0 NOT NULL
);


ALTER TABLE call_classification OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 24922)
-- Name: call_classification_call_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE call_classification_call_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE call_classification_call_category_id_seq OWNER TO postgres;

--
-- TOC entry 2307 (class 0 OID 0)
-- Dependencies: 185
-- Name: call_classification_call_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE call_classification_call_category_id_seq OWNED BY call_classification.call_classification_id;


--
-- TOC entry 187 (class 1259 OID 24928)
-- Name: citizen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE citizen (
    citizen_cpf character varying(12) NOT NULL,
    name character varying(40) NOT NULL,
    phone_number character varying(11),
    email character varying(100),
    enabled integer DEFAULT 0,
    password character varying(32),
    facebook_id character varying(100),
    gender integer,
    neighborhood_id integer,
    public_key character varying(42)
);


ALTER TABLE citizen OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 24932)
-- Name: citizen_login_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE citizen_login_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE citizen_login_id_seq OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 24934)
-- Name: citizen_login; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE citizen_login (
    citizen_login_id integer DEFAULT nextval('citizen_login_id_seq'::regclass) NOT NULL,
    citizen_id character varying(11) NOT NULL,
    token character varying(32) NOT NULL
);


ALTER TABLE citizen_login OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 24924)
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE city (
    city_id integer DEFAULT 0 NOT NULL,
    name character varying(50),
    state_id character varying(2)
);


ALTER TABLE city OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 24938)
-- Name: entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE entity (
    entity_id integer NOT NULL,
    name character varying(40) NOT NULL,
    enabled integer DEFAULT 0,
    icon character varying(50) NOT NULL
);


ALTER TABLE entity OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 24942)
-- Name: entity_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE entity_category (
    entity_category_id integer NOT NULL,
    name character varying(40) NOT NULL,
    enabled integer DEFAULT 0,
    send_message integer DEFAULT 0
);


ALTER TABLE entity_category OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 24947)
-- Name: entity_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE entity_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE entity_category_id_seq OWNER TO postgres;

--
-- TOC entry 2308 (class 0 OID 0)
-- Dependencies: 192
-- Name: entity_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE entity_category_id_seq OWNED BY entity_category.entity_category_id;


--
-- TOC entry 193 (class 1259 OID 24949)
-- Name: entity_entity_category_maps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE entity_entity_category_maps (
    entity_category_id integer NOT NULL,
    entity_id integer NOT NULL
);


ALTER TABLE entity_entity_category_maps OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 24952)
-- Name: entity_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE entity_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE entity_entity_id_seq OWNER TO postgres;

--
-- TOC entry 2309 (class 0 OID 0)
-- Dependencies: 194
-- Name: entity_entity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE entity_entity_id_seq OWNED BY entity.entity_id;


--
-- TOC entry 196 (class 1259 OID 24957)
-- Name: unsolved_call; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unsolved_call (
    unsolved_call_id bigint NOT NULL,
    creation_or_update_date timestamp without time zone NOT NULL,
    citizen_cpf character varying(12),
    call_classification_id integer NOT NULL,
    updated_or_moderated_by character varying(20),
    parent_call_id bigint,
    call_status integer DEFAULT 0,
    call_progress integer DEFAULT 0,
    priority integer DEFAULT 0,
    secret integer DEFAULT 0,
    anonymity integer DEFAULT 0,
    call_source integer,
    no_midia boolean DEFAULT true NOT NULL,
    entity_id integer DEFAULT 2 NOT NULL,
    entity_category_id integer DEFAULT 2 NOT NULL,
    address_id integer DEFAULT 1,
    description integer DEFAULT 1 NOT NULL,
    observation integer,
    qualification smallint,
    remove boolean DEFAULT false NOT NULL
);


ALTER TABLE unsolved_call OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 24970)
-- Name: initial_call_call_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE initial_call_call_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE initial_call_call_id_seq OWNER TO postgres;

--
-- TOC entry 2310 (class 0 OID 0)
-- Dependencies: 197
-- Name: initial_call_call_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE initial_call_call_id_seq OWNED BY unsolved_call.unsolved_call_id;


--
-- TOC entry 198 (class 1259 OID 24972)
-- Name: log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE log_id_seq OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 24974)
-- Name: log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE log (
    log_id bigint DEFAULT nextval('log_id_seq'::regclass) NOT NULL,
    change_date timestamp without time zone NOT NULL,
    operation_type integer NOT NULL,
    information_type integer NOT NULL,
    system_user_username character varying(20) NOT NULL,
    content2 character varying(2000)
);


ALTER TABLE log OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 24981)
-- Name: media; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE media (
    media_id bigint NOT NULL,
    media bytea NOT NULL,
    unsolved_call_id bigint NOT NULL,
    solved_call_id bigint NOT NULL
);


ALTER TABLE media OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 24987)
-- Name: message_model; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE message_model (
    message_model_id integer NOT NULL,
    name character varying(100) NOT NULL,
    subject character varying(100) NOT NULL,
    message_body character varying(4000) NOT NULL,
    enabled integer DEFAULT 0
);


ALTER TABLE message_model OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 24994)
-- Name: messagem_model_secretary_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE messagem_model_secretary_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE messagem_model_secretary_category_id_seq OWNER TO postgres;

--
-- TOC entry 2311 (class 0 OID 0)
-- Dependencies: 202
-- Name: messagem_model_secretary_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE messagem_model_secretary_category_id_seq OWNED BY message_model.message_model_id;


--
-- TOC entry 215 (class 1259 OID 50197)
-- Name: neighborhood_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE neighborhood_id_seq
    START WITH 58223
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE neighborhood_id_seq OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 24900)
-- Name: neighborhood; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE neighborhood (
    neighborhood_id integer DEFAULT nextval('neighborhood_id_seq'::regclass) NOT NULL,
    city_id integer DEFAULT 0 NOT NULL,
    name character varying(72) NOT NULL,
    nome_abrev character varying(36)
);


ALTER TABLE neighborhood OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 25001)
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);


ALTER TABLE persistent_logins OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 59151)
-- Name: read_broadcast_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE read_broadcast_message (
    citizen_cpf character varying(12) NOT NULL,
    broadcast_message_id integer NOT NULL
);


ALTER TABLE read_broadcast_message OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 25004)
-- Name: solved_call; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE solved_call (
    solved_call_id bigint NOT NULL,
    creation_or_update_date timestamp without time zone NOT NULL,
    citizen_cpf character varying(12),
    call_classification_id integer NOT NULL,
    updated_or_moderated_by character varying(20),
    parent_call_id bigint,
    call_status integer DEFAULT 0,
    call_progress integer DEFAULT 0,
    priority integer DEFAULT 0,
    secret integer DEFAULT 0,
    anonymity integer DEFAULT 0,
    call_source integer,
    no_midia boolean DEFAULT true NOT NULL,
    observation integer,
    description integer DEFAULT 1 NOT NULL,
    address_id integer DEFAULT 2,
    entity_id integer DEFAULT 105 NOT NULL,
    entity_category_id integer DEFAULT 2 NOT NULL,
    qualification smallint,
    remove boolean DEFAULT false NOT NULL
);


ALTER TABLE solved_call OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 24954)
-- Name: state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE state (
    state_id character varying(2) NOT NULL,
    name character varying(72) NOT NULL
);


ALTER TABLE state OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 25017)
-- Name: system_parameter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE system_parameter (
    system_parameter_id integer NOT NULL,
    number_of_days_to_solve_a_call integer NOT NULL,
    city_hall_name character varying(60) NOT NULL,
    city_name character varying(30) NOT NULL,
    state_name character varying(30) NOT NULL,
    state_acronym character varying(2) NOT NULL,
    google_maps_key character varying(200) NOT NULL
);


ALTER TABLE system_parameter OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 25020)
-- Name: system_parameter_system_parameter_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE system_parameter_system_parameter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system_parameter_system_parameter_id_seq OWNER TO postgres;

--
-- TOC entry 2312 (class 0 OID 0)
-- Dependencies: 206
-- Name: system_parameter_system_parameter_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE system_parameter_system_parameter_id_seq OWNED BY system_parameter.system_parameter_id;


--
-- TOC entry 207 (class 1259 OID 25022)
-- Name: system_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE system_user (
    system_user_username character varying(20) NOT NULL,
    password character varying(32) NOT NULL,
    name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    commercial_phone character varying(11) NOT NULL,
    personal_phone character varying(11),
    sector character varying(100),
    job_position character varying(100) NOT NULL,
    system_user_profile_id integer NOT NULL,
    works_at_entity integer,
    enabled integer DEFAULT 0
);


ALTER TABLE system_user OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 25026)
-- Name: system_user_profile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE system_user_profile (
    system_user_profile_id integer NOT NULL,
    name character varying(100) NOT NULL,
    enabled integer DEFAULT 0
);


ALTER TABLE system_user_profile OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 25030)
-- Name: system_user_profile_permission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE system_user_profile_permission (
    system_user_profile_permission_id integer NOT NULL,
    name character varying(100) NOT NULL,
    role character varying(60)
);


ALTER TABLE system_user_profile_permission OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 25033)
-- Name: system_user_profile_permission_system_user_profile_permissio443; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE system_user_profile_permission_system_user_profile_permissio443
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system_user_profile_permission_system_user_profile_permissio443 OWNER TO postgres;

--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 210
-- Name: system_user_profile_permission_system_user_profile_permissio443; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE system_user_profile_permission_system_user_profile_permissio443 OWNED BY system_user_profile_permission.system_user_profile_permission_id;


--
-- TOC entry 211 (class 1259 OID 25035)
-- Name: system_user_profile_system_user_profile_id_seq_1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE system_user_profile_system_user_profile_id_seq_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system_user_profile_system_user_profile_id_seq_1 OWNER TO postgres;

--
-- TOC entry 2314 (class 0 OID 0)
-- Dependencies: 211
-- Name: system_user_profile_system_user_profile_id_seq_1; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE system_user_profile_system_user_profile_id_seq_1 OWNED BY system_user_profile.system_user_profile_id;


--
-- TOC entry 212 (class 1259 OID 25037)
-- Name: system_user_profile_system_user_profile_permission_maps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE system_user_profile_system_user_profile_permission_maps (
    system_user_profile_id integer NOT NULL,
    system_user_profile_permission_id integer NOT NULL
);


ALTER TABLE system_user_profile_system_user_profile_permission_maps OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 25040)
-- Name: system_user_system_user_id_seq_2; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE system_user_system_user_id_seq_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system_user_system_user_id_seq_2 OWNER TO postgres;

--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 213
-- Name: system_user_system_user_id_seq_2; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE system_user_system_user_id_seq_2 OWNED BY system_user.system_user_username;


--
-- TOC entry 217 (class 1259 OID 59149)
-- Name: unread_broadcast_message_broadcast_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unread_broadcast_message_broadcast_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unread_broadcast_message_broadcast_message_id_seq OWNER TO postgres;

--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 217
-- Name: unread_broadcast_message_broadcast_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unread_broadcast_message_broadcast_message_id_seq OWNED BY read_broadcast_message.broadcast_message_id;


--
-- TOC entry 216 (class 1259 OID 59133)
-- Name: unread_call; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unread_call (
    unread_call_id bigint NOT NULL,
    unsolved_call_id bigint,
    solved_call_id bigint,
    read boolean DEFAULT false NOT NULL
);


ALTER TABLE unread_call OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 59213)
-- Name: unread_call_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unread_call_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unread_call_id_seq OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 25042)
-- Name: web_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE web_user (
    public_identification_key character varying(32) NOT NULL,
    unsolved_call_id bigint,
    solved_call_id bigint,
    name character varying(40) NOT NULL,
    web_user_cpf character varying(11) NOT NULL,
    email character varying(40) NOT NULL
);


ALTER TABLE web_user OWNER TO postgres;

--
-- TOC entry 2041 (class 2604 OID 25045)
-- Name: broadcast_message_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message ALTER COLUMN broadcast_message_id SET DEFAULT nextval('broadcast_message_broadcast_message_id_seq'::regclass);


--
-- TOC entry 2042 (class 2604 OID 25046)
-- Name: broadcast_message_category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message_category ALTER COLUMN broadcast_message_category_id SET DEFAULT nextval('broadcast_message_category_broadcast_message_category_id_seq_1'::regclass);


--
-- TOC entry 2043 (class 2604 OID 25047)
-- Name: call_classification_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY call_classification ALTER COLUMN call_classification_id SET DEFAULT nextval('call_classification_call_category_id_seq'::regclass);


--
-- TOC entry 2050 (class 2604 OID 25048)
-- Name: entity_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity ALTER COLUMN entity_id SET DEFAULT nextval('entity_entity_id_seq'::regclass);


--
-- TOC entry 2053 (class 2604 OID 25049)
-- Name: entity_category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity_category ALTER COLUMN entity_category_id SET DEFAULT nextval('entity_category_id_seq'::regclass);


--
-- TOC entry 2068 (class 2604 OID 25050)
-- Name: message_model_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_model ALTER COLUMN message_model_id SET DEFAULT nextval('messagem_model_secretary_category_id_seq'::regclass);


--
-- TOC entry 2087 (class 2604 OID 59154)
-- Name: broadcast_message_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY read_broadcast_message ALTER COLUMN broadcast_message_id SET DEFAULT nextval('unread_broadcast_message_broadcast_message_id_seq'::regclass);


--
-- TOC entry 2080 (class 2604 OID 25052)
-- Name: system_parameter_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_parameter ALTER COLUMN system_parameter_id SET DEFAULT nextval('system_parameter_system_parameter_id_seq'::regclass);


--
-- TOC entry 2082 (class 2604 OID 25053)
-- Name: system_user_username; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user ALTER COLUMN system_user_username SET DEFAULT nextval('system_user_system_user_id_seq_2'::regclass);


--
-- TOC entry 2084 (class 2604 OID 25054)
-- Name: system_user_profile_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile ALTER COLUMN system_user_profile_id SET DEFAULT nextval('system_user_profile_system_user_profile_id_seq_1'::regclass);


--
-- TOC entry 2085 (class 2604 OID 25055)
-- Name: system_user_profile_permission_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile_permission ALTER COLUMN system_user_profile_permission_id SET DEFAULT nextval('system_user_profile_permission_system_user_profile_permissio443'::regclass);


--
-- TOC entry 2064 (class 2604 OID 25056)
-- Name: unsolved_call_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call ALTER COLUMN unsolved_call_id SET DEFAULT nextval('initial_call_call_id_seq'::regclass);


--
-- TOC entry 2089 (class 2606 OID 25058)
-- Name: additional_information_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY additional_information
    ADD CONSTRAINT additional_information_pk PRIMARY KEY (additional_information_id);


--
-- TOC entry 2091 (class 2606 OID 25060)
-- Name: address_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pk PRIMARY KEY (address_id);


--
-- TOC entry 2093 (class 2606 OID 25062)
-- Name: attendance_time_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY attendance_time
    ADD CONSTRAINT attendance_time_pk PRIMARY KEY (entity_id);


--
-- TOC entry 2100 (class 2606 OID 25064)
-- Name: broadcast_message_category_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message_category
    ADD CONSTRAINT broadcast_message_category_pk PRIMARY KEY (broadcast_message_category_id);


--
-- TOC entry 2098 (class 2606 OID 25066)
-- Name: broadcast_message_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message
    ADD CONSTRAINT broadcast_message_pk PRIMARY KEY (broadcast_message_id);


--
-- TOC entry 2102 (class 2606 OID 25068)
-- Name: call_classification_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY call_classification
    ADD CONSTRAINT call_classification_pk PRIMARY KEY (call_classification_id);


--
-- TOC entry 2109 (class 2606 OID 25070)
-- Name: citizen_login_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY citizen_login
    ADD CONSTRAINT citizen_login_pk PRIMARY KEY (citizen_login_id);


--
-- TOC entry 2107 (class 2606 OID 25072)
-- Name: citizen_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY citizen
    ADD CONSTRAINT citizen_pk PRIMARY KEY (citizen_cpf);


--
-- TOC entry 2096 (class 2606 OID 25074)
-- Name: log_bairro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY neighborhood
    ADD CONSTRAINT log_bairro_pkey PRIMARY KEY (neighborhood_id);


--
-- TOC entry 2119 (class 2606 OID 25076)
-- Name: log_faixa_uf_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY state
    ADD CONSTRAINT log_faixa_uf_pkey PRIMARY KEY (state_id);


--
-- TOC entry 2105 (class 2606 OID 25078)
-- Name: log_localidade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city
    ADD CONSTRAINT log_localidade_pkey PRIMARY KEY (city_id);


--
-- TOC entry 2123 (class 2606 OID 25080)
-- Name: log_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY log
    ADD CONSTRAINT log_pk PRIMARY KEY (log_id);


--
-- TOC entry 2125 (class 2606 OID 25082)
-- Name: media_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY media
    ADD CONSTRAINT media_pk PRIMARY KEY (media_id);


--
-- TOC entry 2128 (class 2606 OID 25084)
-- Name: message_model_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_model
    ADD CONSTRAINT message_model_pk PRIMARY KEY (message_model_id);


--
-- TOC entry 2130 (class 2606 OID 25088)
-- Name: persistent_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY persistent_logins
    ADD CONSTRAINT persistent_logins_pkey PRIMARY KEY (series);


--
-- TOC entry 2149 (class 2606 OID 59166)
-- Name: read_broadcast_message_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY read_broadcast_message
    ADD CONSTRAINT read_broadcast_message_pk PRIMARY KEY (citizen_cpf, broadcast_message_id);


--
-- TOC entry 2115 (class 2606 OID 25090)
-- Name: secretary_category_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity_category
    ADD CONSTRAINT secretary_category_pk PRIMARY KEY (entity_category_id);


--
-- TOC entry 2112 (class 2606 OID 25092)
-- Name: secretary_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity
    ADD CONSTRAINT secretary_pk PRIMARY KEY (entity_id);


--
-- TOC entry 2117 (class 2606 OID 25094)
-- Name: secretary_secretary_category_maps_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity_entity_category_maps
    ADD CONSTRAINT secretary_secretary_category_maps_pk PRIMARY KEY (entity_category_id, entity_id);


--
-- TOC entry 2132 (class 2606 OID 25096)
-- Name: solved_call_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT solved_call_pk PRIMARY KEY (solved_call_id);


--
-- TOC entry 2134 (class 2606 OID 25098)
-- Name: system_parameter_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_parameter
    ADD CONSTRAINT system_parameter_pk PRIMARY KEY (system_parameter_id);


--
-- TOC entry 2136 (class 2606 OID 25100)
-- Name: system_user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user
    ADD CONSTRAINT system_user_pk PRIMARY KEY (system_user_username);


--
-- TOC entry 2141 (class 2606 OID 25102)
-- Name: system_user_profile_permission_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile_permission
    ADD CONSTRAINT system_user_profile_permission_pk PRIMARY KEY (system_user_profile_permission_id);


--
-- TOC entry 2139 (class 2606 OID 25104)
-- Name: system_user_profile_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile
    ADD CONSTRAINT system_user_profile_pk PRIMARY KEY (system_user_profile_id);


--
-- TOC entry 2143 (class 2606 OID 25106)
-- Name: system_user_profile_system_user_profile_permission_maps_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile_system_user_profile_permission_maps
    ADD CONSTRAINT system_user_profile_system_user_profile_permission_maps_pk PRIMARY KEY (system_user_profile_id, system_user_profile_permission_id);


--
-- TOC entry 2147 (class 2606 OID 59202)
-- Name: unread_call_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unread_call
    ADD CONSTRAINT unread_call_pk PRIMARY KEY (unread_call_id);


--
-- TOC entry 2121 (class 2606 OID 25108)
-- Name: unsolved_call_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT unsolved_call_pk PRIMARY KEY (unsolved_call_id);


--
-- TOC entry 2145 (class 2606 OID 25110)
-- Name: web_user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY web_user
    ADD CONSTRAINT web_user_pk PRIMARY KEY (public_identification_key);


--
-- TOC entry 2094 (class 1259 OID 25111)
-- Name: log_bairro_log_bairro_loc_nu_sequencial; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX log_bairro_log_bairro_loc_nu_sequencial ON neighborhood USING btree (city_id);


--
-- TOC entry 2103 (class 1259 OID 25114)
-- Name: log_localidade_log_localidade_ufe_sg; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX log_localidade_log_localidade_ufe_sg ON city USING btree (state_id);


--
-- TOC entry 2126 (class 1259 OID 25115)
-- Name: message_model_name_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX message_model_name_unique ON message_model USING btree (name);


--
-- TOC entry 2113 (class 1259 OID 25116)
-- Name: secretary_category_name_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX secretary_category_name_unique ON entity_category USING btree (name);


--
-- TOC entry 2110 (class 1259 OID 25117)
-- Name: secretary_name_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX secretary_name_unique ON entity USING btree (name);


--
-- TOC entry 2137 (class 1259 OID 25118)
-- Name: system_user_profile_name_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX system_user_profile_name_unique ON system_user_profile USING btree (name);


--
-- TOC entry 2150 (class 2606 OID 25322)
-- Name: address_neighborhood_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_neighborhood_fk FOREIGN KEY (neighborhood_id) REFERENCES neighborhood(neighborhood_id);


--
-- TOC entry 2152 (class 2606 OID 25124)
-- Name: bairros_cidades_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY neighborhood
    ADD CONSTRAINT bairros_cidades_fk FOREIGN KEY (city_id) REFERENCES city(city_id);


--
-- TOC entry 2153 (class 2606 OID 25129)
-- Name: broadcast_message_category_broadcast_message_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message
    ADD CONSTRAINT broadcast_message_category_broadcast_message_fk FOREIGN KEY (broadcast_message_category_id) REFERENCES broadcast_message_category(broadcast_message_category_id);


--
-- TOC entry 2159 (class 2606 OID 25134)
-- Name: call_category_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT call_category_call_fk FOREIGN KEY (call_classification_id) REFERENCES call_classification(call_classification_id);


--
-- TOC entry 2170 (class 2606 OID 25139)
-- Name: call_classification_solved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT call_classification_solved_call_fk FOREIGN KEY (call_classification_id) REFERENCES call_classification(call_classification_id);


--
-- TOC entry 2160 (class 2606 OID 25144)
-- Name: citizen_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT citizen_call_fk FOREIGN KEY (citizen_cpf) REFERENCES citizen(citizen_cpf);


--
-- TOC entry 2156 (class 2606 OID 25154)
-- Name: citizen_neighborhood_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY citizen
    ADD CONSTRAINT citizen_neighborhood_fk FOREIGN KEY (neighborhood_id) REFERENCES neighborhood(neighborhood_id);


--
-- TOC entry 2171 (class 2606 OID 25159)
-- Name: citizen_solved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT citizen_solved_call_fk FOREIGN KEY (citizen_cpf) REFERENCES citizen(citizen_cpf);


--
-- TOC entry 2155 (class 2606 OID 25327)
-- Name: city_state_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city
    ADD CONSTRAINT city_state_fk FOREIGN KEY (state_id) REFERENCES state(state_id);


--
-- TOC entry 2151 (class 2606 OID 25169)
-- Name: entity_attendance_time_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY attendance_time
    ADD CONSTRAINT entity_attendance_time_fk FOREIGN KEY (entity_id) REFERENCES entity(entity_id);


--
-- TOC entry 2158 (class 2606 OID 59297)
-- Name: entity_category_eecm_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity_entity_category_maps
    ADD CONSTRAINT entity_category_eecm_fk FOREIGN KEY (entity_category_id) REFERENCES entity_category(entity_category_id) ON UPDATE CASCADE;


--
-- TOC entry 2172 (class 2606 OID 25179)
-- Name: entity_entity_category_maps_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT entity_entity_category_maps_fk FOREIGN KEY (entity_id, entity_category_id) REFERENCES entity_entity_category_maps(entity_id, entity_category_id);


--
-- TOC entry 2166 (class 2606 OID 59287)
-- Name: entity_entity_category_maps_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT entity_entity_category_maps_fk FOREIGN KEY (entity_id, entity_category_id) REFERENCES entity_entity_category_maps(entity_id, entity_category_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2157 (class 2606 OID 59292)
-- Name: entity_entity_entity_category_maps_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entity_entity_category_maps
    ADD CONSTRAINT entity_entity_entity_category_maps_fk FOREIGN KEY (entity_id) REFERENCES entity(entity_id) ON UPDATE CASCADE;


--
-- TOC entry 2168 (class 2606 OID 25189)
-- Name: initial_call_media_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY media
    ADD CONSTRAINT initial_call_media_fk FOREIGN KEY (unsolved_call_id) REFERENCES unsolved_call(unsolved_call_id);


--
-- TOC entry 2167 (class 2606 OID 25194)
-- Name: log_system_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY log
    ADD CONSTRAINT log_system_user_fk FOREIGN KEY (system_user_username) REFERENCES system_user(system_user_username);


--
-- TOC entry 2178 (class 2606 OID 25209)
-- Name: secretary_system_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user
    ADD CONSTRAINT secretary_system_user_fk FOREIGN KEY (works_at_entity) REFERENCES entity(entity_id);


--
-- TOC entry 2173 (class 2606 OID 25214)
-- Name: solved_call_additional_information_description_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT solved_call_additional_information_description_fk FOREIGN KEY (description) REFERENCES additional_information(additional_information_id);


--
-- TOC entry 2174 (class 2606 OID 25219)
-- Name: solved_call_address_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT solved_call_address_fk FOREIGN KEY (address_id) REFERENCES address(address_id);


--
-- TOC entry 2169 (class 2606 OID 25224)
-- Name: solved_call_media_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY media
    ADD CONSTRAINT solved_call_media_fk FOREIGN KEY (solved_call_id) REFERENCES solved_call(solved_call_id);


--
-- TOC entry 2154 (class 2606 OID 25229)
-- Name: system_user_broadcast_message_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY broadcast_message
    ADD CONSTRAINT system_user_broadcast_message_fk FOREIGN KEY (created_by) REFERENCES system_user(system_user_username);


--
-- TOC entry 2161 (class 2606 OID 25234)
-- Name: system_user_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT system_user_call_fk FOREIGN KEY (updated_or_moderated_by) REFERENCES system_user(system_user_username);


--
-- TOC entry 2180 (class 2606 OID 25239)
-- Name: system_user_profile_permission_system_user_profile_system_us303; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile_system_user_profile_permission_maps
    ADD CONSTRAINT system_user_profile_permission_system_user_profile_system_us303 FOREIGN KEY (system_user_profile_permission_id) REFERENCES system_user_profile_permission(system_user_profile_permission_id);


--
-- TOC entry 2179 (class 2606 OID 25244)
-- Name: system_user_profile_system_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user
    ADD CONSTRAINT system_user_profile_system_user_fk FOREIGN KEY (system_user_profile_id) REFERENCES system_user_profile(system_user_profile_id);


--
-- TOC entry 2181 (class 2606 OID 25249)
-- Name: system_user_profile_system_user_profile_system_user_profile_419; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY system_user_profile_system_user_profile_permission_maps
    ADD CONSTRAINT system_user_profile_system_user_profile_system_user_profile_419 FOREIGN KEY (system_user_profile_id) REFERENCES system_user_profile(system_user_profile_id);


--
-- TOC entry 2175 (class 2606 OID 25254)
-- Name: system_user_solved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT system_user_solved_call_fk FOREIGN KEY (updated_or_moderated_by) REFERENCES system_user(system_user_username);


--
-- TOC entry 2187 (class 2606 OID 59160)
-- Name: unread_broadcast_message_broadcast_message_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY read_broadcast_message
    ADD CONSTRAINT unread_broadcast_message_broadcast_message_fk FOREIGN KEY (broadcast_message_id) REFERENCES broadcast_message(broadcast_message_id);


--
-- TOC entry 2186 (class 2606 OID 59155)
-- Name: unread_broadcast_message_citizen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY read_broadcast_message
    ADD CONSTRAINT unread_broadcast_message_citizen_fk FOREIGN KEY (citizen_cpf) REFERENCES citizen(citizen_cpf);


--
-- TOC entry 2184 (class 2606 OID 59203)
-- Name: unread_call_solved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unread_call
    ADD CONSTRAINT unread_call_solved_call_fk FOREIGN KEY (solved_call_id) REFERENCES solved_call(solved_call_id);


--
-- TOC entry 2185 (class 2606 OID 59208)
-- Name: unread_call_unsolved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unread_call
    ADD CONSTRAINT unread_call_unsolved_call_fk FOREIGN KEY (unsolved_call_id) REFERENCES unsolved_call(unsolved_call_id);


--
-- TOC entry 2162 (class 2606 OID 25259)
-- Name: unsolved_call_additional_information_description_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT unsolved_call_additional_information_description_fk FOREIGN KEY (description) REFERENCES additional_information(additional_information_id);


--
-- TOC entry 2163 (class 2606 OID 25264)
-- Name: unsolved_call_additional_information_observation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT unsolved_call_additional_information_observation_fk FOREIGN KEY (observation) REFERENCES additional_information(additional_information_id);


--
-- TOC entry 2176 (class 2606 OID 25269)
-- Name: unsolved_call_additional_information_observation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT unsolved_call_additional_information_observation_fk FOREIGN KEY (observation) REFERENCES additional_information(additional_information_id);


--
-- TOC entry 2164 (class 2606 OID 25274)
-- Name: unsolved_call_address_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT unsolved_call_address_fk FOREIGN KEY (address_id) REFERENCES address(address_id);


--
-- TOC entry 2177 (class 2606 OID 25279)
-- Name: unsolved_call_unsolved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solved_call
    ADD CONSTRAINT unsolved_call_unsolved_call_fk FOREIGN KEY (parent_call_id) REFERENCES solved_call(solved_call_id);


--
-- TOC entry 2165 (class 2606 OID 25284)
-- Name: unsolved_call_unsolved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unsolved_call
    ADD CONSTRAINT unsolved_call_unsolved_call_fk FOREIGN KEY (parent_call_id) REFERENCES unsolved_call(unsolved_call_id);


--
-- TOC entry 2182 (class 2606 OID 25289)
-- Name: web_user_solved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY web_user
    ADD CONSTRAINT web_user_solved_call_fk FOREIGN KEY (solved_call_id) REFERENCES solved_call(solved_call_id);


--
-- TOC entry 2183 (class 2606 OID 25294)
-- Name: web_user_unsolved_call_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY web_user
    ADD CONSTRAINT web_user_unsolved_call_fk FOREIGN KEY (unsolved_call_id) REFERENCES unsolved_call(unsolved_call_id);


--
-- TOC entry 2303 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-02-03 09:07:45

--
-- PostgreSQL database dump complete
--

