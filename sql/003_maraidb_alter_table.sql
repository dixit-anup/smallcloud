ALTER TABLE articles ADD FULLTEXT INDEX fx_title (article_title);
ALTER TABLE articles ADD FULLTEXT INDEX fx_content (article_content);
ALTER TABLE articles ADD FULLTEXT INDEX fx_writer (writer_name);