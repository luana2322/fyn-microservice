package com.fyn.user.service;

import com.fyn.common.dto.user.UserSummary;
import com.fyn.user.model.User;
import com.fyn.user.model.UserProfile;
import com.fyn.user.repository.ProfileRepository;
import com.fyn.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

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
}
