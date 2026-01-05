package com.fyn.common.event.message;

import com.fyn.common.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent extends DomainEvent {
    private String messageId;
    private String conversationId;
    private String senderId;
    private String recipientId;
    private String content;
    private String preview;

    @Override
    public String getEventType() {
        return "MESSAGE_SENT";
    }
}
