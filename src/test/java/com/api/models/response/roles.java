package com.api.models.response;

public class roles {
	
	private String ROLE_USER;

	public roles(String rOLE_USER) {
		super();
		ROLE_USER = rOLE_USER;
	}

	public String getROLE_USER() {
		return ROLE_USER;
	}

	public void setROLE_USER(String rOLE_USER) {
		ROLE_USER = rOLE_USER;
	}

	@Override
	public String toString() {
		return "roles [ROLE_USER=" + ROLE_USER + "]";
	}
	
	

}
