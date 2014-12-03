ALTER TABLE articles ADD FULLTEXT INDEX fx_title_content_write 
	(article_title, article_content, writer_name);