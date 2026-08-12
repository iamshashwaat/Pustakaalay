package com.pustakaalay.dto;

import com.pustakaalay.entity.User;

import java.time.LocalDateTime;

public class UserResponse {

    private final Long id;
    private final Long roleId;
    private final String roleName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String membershipNumber;
    private final User.UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime lastLoginAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.roleId = user.getRole().getId();
        this.roleName = user.getRole().getName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.membershipNumber = user.getMembershipNumber();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.lastLoginAt = user.getLastLoginAt();
    }

    public Long getId() { return id; }
    public Long getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getMembershipNumber() { return membershipNumber; }
    public User.UserStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
}
