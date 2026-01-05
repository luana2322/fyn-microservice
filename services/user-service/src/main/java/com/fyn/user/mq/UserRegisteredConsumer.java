package com.fyn.user.mq;

import com.fyn.common.event.user.UserRegisteredEvent;
import com.fyn.user.model.User;
import com.fyn.user.model.UserProfile;
import com.fyn.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {
    private final UserRepository userRepository;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "user-service.registration.queue", durable = "true"), exchange = @Exchange(name = "user-exchange", type = "topic"), key = "user.registered"))
    @Transactional
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent: {}", event.getUserId());

        User user = User.builder()
                .id(UUID.fromString(event.getUserId()))
                .username(event.getEmail().split("@")[0]) // Temporary username if not provided
                .email(event.getEmail())
                .build();

        UserProfile profile = UserProfile.builder()
                .user(user)
                .fullName(event.getFullName())
                .build();

        user.setProfile(profile);
        userRepository.save(user);
    }
}
