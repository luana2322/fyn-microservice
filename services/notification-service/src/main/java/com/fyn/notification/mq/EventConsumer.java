package com.fyn.notification.mq;

import com.fyn.common.event.user.UserRegisteredEvent;
import com.fyn.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final NotificationService notificationService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "notification-service.registration.queue", durable = "true"), exchange = @Exchange(name = "user-exchange", type = "topic"), key = "user.registered"))
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("Processing UserRegisteredEvent for notification: {}", event.getUserId());
        notificationService.sendNotification(
                UUID.fromString(event.getUserId()),
                "Welcome to FYN!",
                "Hello " + event.getFullName() + ", your account has been successfully created.",
                "WELCOME");
    }
}
