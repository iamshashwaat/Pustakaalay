package com.pustakaalay.dto;

import com.pustakaalay.entity.User;

public class UserSummaryResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String membershipNumber;
    private final String role;
    private final User.UserStatus status;

    public UserSummaryResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.membershipNumber = user.getMembershipNumber();
        this.role = user.getRole().getName();
        this.status = user.getStatus();
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getMembershipNumber() { return membershipNumber; }
    public String getRole() { return role; }
    public User.UserStatus getStatus() { return status; }
}
