/*****************************************************
 * partition, full-text searching 
 *****************************************************/

use smallcloud;

DROP TABLE IF EXISTS article_upload;
DROP TABLE IF EXISTS articles;

CREATE TABLE IF NOT EXISTS articles (
	article_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	article_title VARCHAR(256) NOT NULL,
	article_content TEXT NOT NULL,
	writer_name VARCHAR(256) NOT NULL,
	password VARCHAR(50) NOT NULL,
	homepage VARCHAR(100),
	write_time DATETIME NOT NULL,
	parent_id INT UNSIGNED DEFAULT 0,
	level TINYINT UNSIGNED DEFAULT 1,
	write_ip VARCHAR(16) NOT NULL,
	mask BOOLEAN NOT NULL DEFAULT 0,
	
	INDEX ix_write_time (write_time)
) default character set=utf8;


CREATE TABLE IF NOT EXISTS article_upload (
	upload_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	article_id INT UNSIGNED NOT NULL,
	file_name VARCHAR(256) NOT NULL,
	
	INDEX ix_article_id (article_id),
	FOREIGN KEY(article_id) REFERENCES articles(article_id)
) default character set=utf8;
