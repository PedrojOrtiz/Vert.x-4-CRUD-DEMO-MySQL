CREATE TABLE IF NOT EXISTS
  books ( id         VARCHAR(255) DEFAULT '1' NOT NULL,
          author     VARCHAR(255) NOT NULL,
          country    VARCHAR(255),
          image_link VARCHAR(255),
          language   VARCHAR(255),
          link       VARCHAR(255),
          pages      INT(4),
          title      VARCHAR(255) NOT NULL,
          year       INT(4),
          active     TINYINT DEFAULT 1,
          PRIMARY KEY(id)
  );
