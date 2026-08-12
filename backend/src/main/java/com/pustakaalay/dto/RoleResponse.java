package com.pustakaalay.dto;

import com.pustakaalay.entity.Role;

public class RoleResponse {

    private final Long id;
    private final String name;
    private final String description;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
