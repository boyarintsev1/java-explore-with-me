DROP TABLE if exists stats;

CREATE TABLE IF NOT EXISTS stats (                 -- создание таблицы статистики посещений EndpointHits
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR(30) NOT NULL,
  uri VARCHAR(30) NOT NULL,
  ip VARCHAR(20) NOT NULL,
  created timestamp NOT NULL UNIQUE,
  CONSTRAINT pk_stats_user PRIMARY KEY (id)
);