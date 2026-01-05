package com.fyn.common.event.user;

import com.fyn.common.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent extends DomainEvent {
    private String userId;
    private String email;
    private String fullName;

    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }
}
