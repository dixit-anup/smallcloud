package com.mamascode.smallcloud.repository.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DaoTestSetup {
	@Autowired JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int deleteAllArticle() {
		return jdbcTemplate.update("DELETE FROM articles");
	}
}
