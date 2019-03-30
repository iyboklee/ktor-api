DROP TABLE IF EXISTS artists CASCADE;
DROP TABLE IF EXISTS albums CASCADE;
DROP TABLE IF EXISTS songs CASCADE;

CREATE TABLE artists (
  seq bigint NOT NULL AUTO_INCREMENT,
  name varchar(50) NOT NULL,
  members int NOT NULL DEFAULT 1,
  debut_at date NOT NULL,
  PRIMARY KEY (seq),
  CONSTRAINT uq_artist_name UNIQUE (name)
);

INSERT INTO artists (name, members, debut_at) values ('TWICE', 9, '2015-10-20');
INSERT INTO artists (name, members, debut_at) values ('BTS', 7, '2013-06-12');
INSERT INTO artists (name, members, debut_at) values ('BLACKPINK', 4, '2016-08-08');
INSERT INTO artists (name, members, debut_at) values ('MOMOLAND', 9, '2016-11-10');

CREATE TABLE albums (
  seq bigint NOT NULL AUTO_INCREMENT,
  artist_seq bigint  NOT NULL,
  title varchar(50) NOT NULL,
  genre varchar(50) DEFAULT NULL,
  issue_at date NOT NULL,
  PRIMARY KEY (seq),
  CONSTRAINT uq_artist_album_title UNIQUE (artist_seq, title),
  CONSTRAINT fk_album_to_artist FOREIGN KEY (artist_seq) REFERENCES artists (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (1, 'YES or YES', '댄스, 발라드', '2018-11-05');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (1, 'What is Love?', '댄스', '2018-04-09');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (1, 'SIGNAL', '댄스', '2017-05-15');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (2, 'LOVE YOURSELF 結 Answer', '랩/힙합', '2018-08-24');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (2, 'WINGS', '랩/힙합', '2016-10-10');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (2, 'DARK&WILD', '랩/힙합', '2014-08-20');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (3, 'SQUARE UP', '댄스', '2018-06-15');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (3, '마지막처럼', '댄스', '2017-06-22');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (3, 'SQUARE TWO', '댄스', '2016-11-01');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (4, 'Fun to The World', '댄스', '2018-06-26');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (4, 'GREAT!', '댄스', '2018-01-03');
INSERT INTO albums (artist_seq, title, genre, issue_at) VALUES (4, 'Welcome to MOMOLAND', '댄스', '2016-11-10');

CREATE TABLE songs (
  seq bigint NOT NULL AUTO_INCREMENT,
  artist_seq bigint  NOT NULL,
  album_seq bigint  NOT NULL,
  title varchar(50) NOT NULL,
  PRIMARY KEY (seq),
  CONSTRAINT fk_songs_to_artist FOREIGN KEY (artist_seq) REFERENCES artists (seq) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_songs_to_album FOREIGN KEY (album_seq) REFERENCES albums (seq) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO songs (artist_seq, album_seq, title) values (1, 1, 'YES or YES');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 1, 'BDZ (Korean Ver.)');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 1, 'SAY YOU LOVE ME');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 2, 'What is Love?');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 2, 'SWEET TALKER');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 2, 'HO!');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 3, 'SIGNAL');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 3, '하루에 세번');
INSERT INTO songs (artist_seq, album_seq, title) values (1, 3, 'ONLY 너');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 4, 'IDOL');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 4, 'Trivia 轉 : Seesaw');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 4, 'FAKE LOVE');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 5, '피 땀 눈물');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 5, 'MAMA');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 5, '21세기 소녀');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 6, 'Danger');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 6, '힙합성애자');
INSERT INTO songs (artist_seq, album_seq, title) values (2, 6, 'BTS Cypher PT.3 : KILLER (Feat. Supreme Boi)');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 7, '뚜두뚜두 (DDU-DU DDU-DU)');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 7, 'Forever Young');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 7, 'Really');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 8, '마지막처럼');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 9, '불장난');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 9, 'STAY');
INSERT INTO songs (artist_seq, album_seq, title) values (3, 9, '휘파람 (Acoustic Ver.)');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 10, 'BAAM');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 10, '베리베리');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 10, '빙고게임');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 11, '뿜뿜');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 11, '궁금해');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 11, 'Same Same');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 12, '짠쿵쾅');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 12, '상사병');
INSERT INTO songs (artist_seq, album_seq, title) values (4, 12, '어기여차');