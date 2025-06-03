package com.api.models.response;

public class RolesResponse {

    private String ROLE_USER;

    // Correct constructor name and assignment
    public RolesResponse(String ROLE_USER) {
        this.ROLE_USER = ROLE_USER;
    }

    public String getROLE_USER() {
        return ROLE_USER;
    }

    public void setROLE_USER(String ROLE_USER) {
        this.ROLE_USER = ROLE_USER;
    }

    @Override
    public String toString() {
        return "RolesResponse [ROLE_USER=" + ROLE_USER + "]";
    }
}
