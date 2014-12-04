/*****************************************************
 * 1. fulltext 검색: 기대한 대로 작동하지 않음
 *****************************************************/

/*
ALTER TABLE articles ADD FULLTEXT INDEX fx_title (article_title);
ALTER TABLE articles ADD FULLTEXT INDEX fx_content (article_content);
ALTER TABLE articles ADD FULLTEXT INDEX fx_writer (writer_name);
*/