package com.mamascode.smallcloud.repository.mybatis;

public abstract class MyBatisDao {
	protected String namespace;
	private String BASE_NAMESPACE = "com.mamascode.smallcloud.mybatis.mapper.";
	
	public MyBatisDao(String namespace) {
		this.namespace = new StringBuilder()
				.append(BASE_NAMESPACE).append(namespace).toString();
	}

	///// getMapperId: 맵퍼의 SQL 아이디와 네임스페이스를 연결해줌 
	protected String getMapperId(String mapperId) {
		return String.format("%s.%s", namespace, mapperId);
	}
}
