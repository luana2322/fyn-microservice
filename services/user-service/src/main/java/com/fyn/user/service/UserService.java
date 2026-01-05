package com.fyn.user.service;

import com.fyn.common.dto.user.UserSummary;
import com.fyn.user.dto.UserDto;
import com.fyn.user.model.User;
import com.fyn.user.model.UserProfile;
import com.fyn.user.repository.ProfileRepository;
import com.fyn.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        UserProfile profile = user.getProfile();

        return UserDto.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .status("ACTIVE")
                .role("USER")
                .profile(profile != null ? UserDto.ProfileDto.builder()
                        .fullName(profile.getFullName())
                        .bio(profile.getBio())
                        .avatarUrl(profile.getAvatarUrl())
                        .location(profile.getLocation())
                        .isPrivate(false)
                        .build() : UserDto.ProfileDto.builder().isPrivate(false).build())
                .build();
    }

    public UserProfile getProfile(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Transactional
    public UserProfile updateProfile(UUID userId, UserProfile profileData) {
        UserProfile existingProfile = getProfile(userId);
        existingProfile.setFullName(profileData.getFullName());
        existingProfile.setBio(profileData.getBio());
        existingProfile.setAvatarUrl(profileData.getAvatarUrl());
        existingProfile.setLocation(profileData.getLocation());
        return profileRepository.save(existingProfile);
    }

    public UserSummary getUserSummary(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        UserProfile profile = user.getProfile();
        return UserSummary.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .fullName(profile != null ? profile.getFullName() : null)
                .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .build();
    }

    public List<UserSummary> searchUsers(String query, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findByUsernameContainingIgnoreCase(query, pageRequest)
                .getContent()
                .stream()
                .map(user -> {
                    UserProfile profile = user.getProfile();
                    return UserSummary.builder()
                            .id(user.getId().toString())
                            .username(user.getUsername())
                            .fullName(profile != null ? profile.getFullName() : null)
                            .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                            .build();
                })
                .toList();
    }
}
