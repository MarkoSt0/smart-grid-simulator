--
-- PostgreSQL database dump
--

\restrict edFT7LWE2Z2JgX1UGVX2xx3Jgna0HD5ofmyidA4oF8ZbKqfEF0BM6CxAdOqG4rU

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-03-24 23:35:50

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 229 (class 1259 OID 16539)
-- Name: alerts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.alerts (
    id bigint NOT NULL,
    alert_type character varying(50) NOT NULL,
    severity character varying(20) NOT NULL,
    description text,
    resolved boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    resolved_at timestamp with time zone,
    CONSTRAINT alerts_severity_check CHECK (((severity)::text = ANY ((ARRAY['WARNING'::character varying, 'CRITICAL'::character varying])::text[])))
);


ALTER TABLE public.alerts OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16538)
-- Name: alerts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.alerts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.alerts_id_seq OWNER TO postgres;

--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 228
-- Name: alerts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.alerts_id_seq OWNED BY public.alerts.id;


--
-- TOC entry 227 (class 1259 OID 16526)
-- Name: commands; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commands (
    id bigint NOT NULL,
    command_type character varying(50) NOT NULL,
    target_node character varying(50),
    value numeric(10,2),
    issued_by character varying(50),
    executed_at timestamp with time zone DEFAULT now(),
    metadata jsonb
);


ALTER TABLE public.commands OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16525)
-- Name: commands_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.commands_id_seq OWNER TO postgres;

--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 226
-- Name: commands_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commands_id_seq OWNED BY public.commands.id;


--
-- TOC entry 225 (class 1259 OID 16512)
-- Name: edge_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.edge_state (
    edge_id integer NOT NULL,
    current_flow_mw numeric(10,2) DEFAULT 0,
    utilization_percent numeric(5,2),
    last_updated timestamp with time zone DEFAULT now()
);


ALTER TABLE public.edge_state OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16460)
-- Name: edges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.edges (
    id integer NOT NULL,
    from_node character varying(50),
    to_node character varying(50),
    limit_mw numeric(10,2) NOT NULL
);


ALTER TABLE public.edges OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16459)
-- Name: edges_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.edges_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.edges_id_seq OWNER TO postgres;

--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 220
-- Name: edges_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.edges_id_seq OWNED BY public.edges.id;


--
-- TOC entry 224 (class 1259 OID 16499)
-- Name: node_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.node_state (
    node_id character varying(50) NOT NULL,
    current_mw numeric(10,2) DEFAULT 0,
    current_soc_percent numeric(5,2),
    last_updated timestamp with time zone DEFAULT now()
);


ALTER TABLE public.node_state OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16448)
-- Name: nodes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nodes (
    id character varying(50) NOT NULL,
    type character varying(20) NOT NULL,
    max_capacity_mw numeric(10,2),
    capacity_mwh numeric(10,2),
    base_load numeric(10,2),
    metadata jsonb,
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT nodes_type_check CHECK (((type)::text = ANY ((ARRAY['SOURCE'::character varying, 'CONSUMER'::character varying, 'HUB'::character varying, 'SUBSTATION'::character varying, 'STORAGE'::character varying])::text[])))
);


ALTER TABLE public.nodes OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16481)
-- Name: telemetry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telemetry (
    id bigint NOT NULL,
    node_id character varying(50),
    "timestamp" timestamp with time zone DEFAULT now(),
    reported_mw numeric(10,2) NOT NULL,
    metadata jsonb
);


ALTER TABLE public.telemetry OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16480)
-- Name: telemetry_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.telemetry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.telemetry_id_seq OWNER TO postgres;

--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 222
-- Name: telemetry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.telemetry_id_seq OWNED BY public.telemetry.id;


--
-- TOC entry 4893 (class 2604 OID 16542)
-- Name: alerts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alerts ALTER COLUMN id SET DEFAULT nextval('public.alerts_id_seq'::regclass);


--
-- TOC entry 4891 (class 2604 OID 16529)
-- Name: commands id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commands ALTER COLUMN id SET DEFAULT nextval('public.commands_id_seq'::regclass);


--
-- TOC entry 4884 (class 2604 OID 16463)
-- Name: edges id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edges ALTER COLUMN id SET DEFAULT nextval('public.edges_id_seq'::regclass);


--
-- TOC entry 4885 (class 2604 OID 16484)
-- Name: telemetry id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telemetry ALTER COLUMN id SET DEFAULT nextval('public.telemetry_id_seq'::regclass);


--
-- TOC entry 5080 (class 0 OID 16539)
-- Dependencies: 229
-- Data for Name: alerts; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5078 (class 0 OID 16526)
-- Dependencies: 227
-- Data for Name: commands; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5076 (class 0 OID 16512)
-- Dependencies: 225
-- Data for Name: edge_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.edge_state VALUES (1, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (2, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (3, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (4, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (5, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (6, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (7, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (8, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (9, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.edge_state VALUES (10, 0.00, 0.00, '2026-03-22 20:17:27.899443+01');


--
-- TOC entry 5072 (class 0 OID 16460)
-- Dependencies: 221
-- Data for Name: edges; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.edges VALUES (1, 'WIND_N', 'MAIN_HUB', 60.00);
INSERT INTO public.edges VALUES (2, 'WIND_S', 'MAIN_HUB', 60.00);
INSERT INTO public.edges VALUES (3, 'HYDRO_1', 'MAIN_HUB', 40.00);
INSERT INTO public.edges VALUES (4, 'MAIN_HUB', 'BAT_MAIN', 100.00);
INSERT INTO public.edges VALUES (5, 'MAIN_HUB', 'SUB_A', 70.00);
INSERT INTO public.edges VALUES (6, 'MAIN_HUB', 'SUB_B', 70.00);
INSERT INTO public.edges VALUES (7, 'SUB_A', 'RES_ZONE_1', 50.00);
INSERT INTO public.edges VALUES (8, 'SUB_B', 'RES_ZONE_2', 50.00);
INSERT INTO public.edges VALUES (9, 'SUB_B', 'BAT_EDGE', 40.00);
INSERT INTO public.edges VALUES (10, 'SUB_A', 'SUB_B', 30.00);


--
-- TOC entry 5075 (class 0 OID 16499)
-- Dependencies: 224
-- Data for Name: node_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.node_state VALUES ('WIND_N', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('WIND_S', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('HYDRO_1', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('MAIN_HUB', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('SUB_A', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('SUB_B', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('RES_ZONE_1', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('RES_ZONE_2', 0.00, NULL, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('BAT_MAIN', 0.00, 50.00, '2026-03-22 20:17:27.899443+01');
INSERT INTO public.node_state VALUES ('BAT_EDGE', 0.00, 30.00, '2026-03-22 20:17:27.899443+01');


--
-- TOC entry 5070 (class 0 OID 16448)
-- Dependencies: 219
-- Data for Name: nodes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.nodes VALUES ('WIND_N', 'SOURCE', 50.00, NULL, NULL, '{"role": "Primarni izvor, varijabilan", "description": "Severna vetrenjača"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('WIND_S', 'SOURCE', 50.00, NULL, NULL, '{"role": "Sekundarni izvor, varijabilan", "description": "Južna vetrenjača"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('HYDRO_1', 'SOURCE', 30.00, NULL, NULL, '{"role": "Stabilan bazni izvor", "description": "Mala hidroelektrana"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('MAIN_HUB', 'HUB', 150.00, NULL, NULL, '{"role": "Sabira svu energiju i šalje je dalje", "description": "Glavna trafostanica"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('SUB_A', 'SUBSTATION', 80.00, NULL, NULL, '{"role": "Razvodi struju za prvu zonu kuća", "description": "Trafostanica Sever"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('SUB_B', 'SUBSTATION', 80.00, NULL, NULL, '{"role": "Razvodi struju za drugu zonu kuća", "description": "Trafostanica Jug"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('RES_ZONE_1', 'CONSUMER', NULL, NULL, 40.00, '{"role": "Dinamička potrošnja (jutro/veče)", "description": "Agregirano 750 kuća"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('RES_ZONE_2', 'CONSUMER', NULL, NULL, 40.00, '{"role": "Dinamička potrošnja (jutro/veče)", "description": "Agregirano 750 kuća"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('BAT_MAIN', 'STORAGE', 50.00, 100.00, NULL, '{"role": "Nalazi se uz HUB, pegla ulaznu snagu", "location": "main_hub", "description": "Sistemska baterija"}', '2026-03-22 20:17:27.899443+01');
INSERT INTO public.nodes VALUES ('BAT_EDGE', 'STORAGE', 40.00, 40.00, NULL, '{"role": "Čuva SUB_B od preopterećenja", "location": "sub_b", "description": "Lokalna baterija"}', '2026-03-22 20:17:27.899443+01');


--
-- TOC entry 5074 (class 0 OID 16481)
-- Dependencies: 223
-- Data for Name: telemetry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 228
-- Name: alerts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.alerts_id_seq', 1, false);


--
-- TOC entry 5091 (class 0 OID 0)
-- Dependencies: 226
-- Name: commands_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commands_id_seq', 1, false);


--
-- TOC entry 5092 (class 0 OID 0)
-- Dependencies: 220
-- Name: edges_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.edges_id_seq', 10, true);


--
-- TOC entry 5093 (class 0 OID 0)
-- Dependencies: 222
-- Name: telemetry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.telemetry_id_seq', 1, false);


--
-- TOC entry 4916 (class 2606 OID 16552)
-- Name: alerts alerts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alerts
    ADD CONSTRAINT alerts_pkey PRIMARY KEY (id);


--
-- TOC entry 4913 (class 2606 OID 16536)
-- Name: commands commands_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commands
    ADD CONSTRAINT commands_pkey PRIMARY KEY (id);


--
-- TOC entry 4911 (class 2606 OID 16519)
-- Name: edge_state edge_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edge_state
    ADD CONSTRAINT edge_state_pkey PRIMARY KEY (edge_id);


--
-- TOC entry 4901 (class 2606 OID 16469)
-- Name: edges edges_from_node_to_node_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edges
    ADD CONSTRAINT edges_from_node_to_node_key UNIQUE (from_node, to_node);


--
-- TOC entry 4903 (class 2606 OID 16467)
-- Name: edges edges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edges
    ADD CONSTRAINT edges_pkey PRIMARY KEY (id);


--
-- TOC entry 4909 (class 2606 OID 16506)
-- Name: node_state node_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.node_state
    ADD CONSTRAINT node_state_pkey PRIMARY KEY (node_id);


--
-- TOC entry 4899 (class 2606 OID 16458)
-- Name: nodes nodes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nodes
    ADD CONSTRAINT nodes_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 16491)
-- Name: telemetry telemetry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telemetry
    ADD CONSTRAINT telemetry_pkey PRIMARY KEY (id);


--
-- TOC entry 4917 (class 1259 OID 16553)
-- Name: idx_alerts_unresolved; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_alerts_unresolved ON public.alerts USING btree (created_at DESC) WHERE (resolved = false);


--
-- TOC entry 4914 (class 1259 OID 16537)
-- Name: idx_commands_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_commands_time ON public.commands USING btree (executed_at DESC);


--
-- TOC entry 4904 (class 1259 OID 16498)
-- Name: idx_telemetry_node; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_telemetry_node ON public.telemetry USING btree (node_id, "timestamp" DESC);


--
-- TOC entry 4905 (class 1259 OID 16497)
-- Name: idx_telemetry_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_telemetry_time ON public.telemetry USING btree ("timestamp" DESC);


--
-- TOC entry 4922 (class 2606 OID 16520)
-- Name: edge_state edge_state_edge_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edge_state
    ADD CONSTRAINT edge_state_edge_id_fkey FOREIGN KEY (edge_id) REFERENCES public.edges(id);


--
-- TOC entry 4918 (class 2606 OID 16470)
-- Name: edges edges_from_node_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edges
    ADD CONSTRAINT edges_from_node_fkey FOREIGN KEY (from_node) REFERENCES public.nodes(id) ON DELETE CASCADE;


--
-- TOC entry 4919 (class 2606 OID 16475)
-- Name: edges edges_to_node_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.edges
    ADD CONSTRAINT edges_to_node_fkey FOREIGN KEY (to_node) REFERENCES public.nodes(id) ON DELETE CASCADE;


--
-- TOC entry 4921 (class 2606 OID 16507)
-- Name: node_state node_state_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.node_state
    ADD CONSTRAINT node_state_node_id_fkey FOREIGN KEY (node_id) REFERENCES public.nodes(id);


--
-- TOC entry 4920 (class 2606 OID 16492)
-- Name: telemetry telemetry_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telemetry
    ADD CONSTRAINT telemetry_node_id_fkey FOREIGN KEY (node_id) REFERENCES public.nodes(id);


-- Completed on 2026-03-24 23:35:50

--
-- PostgreSQL database dump complete
--

\unrestrict edFT7LWE2Z2JgX1UGVX2xx3Jgna0HD5ofmyidA4oF8ZbKqfEF0BM6CxAdOqG4rU

