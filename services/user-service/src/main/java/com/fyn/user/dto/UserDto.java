package com.fyn.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String status;
    private String role;
    private ProfileDto profile;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileDto {
        private String fullName;
        private String bio;
        private String avatarUrl;
        private String location;
        @Builder.Default
        private boolean isPrivate = false;
    }
}
