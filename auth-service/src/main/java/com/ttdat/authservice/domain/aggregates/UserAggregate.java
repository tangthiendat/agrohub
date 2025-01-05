package com.ttdat.authservice.domain.aggregates;

import com.ttdat.authservice.application.commands.CreateUserCommand;
import com.ttdat.authservice.domain.entities.Gender;
import com.ttdat.authservice.domain.events.UserCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate(type = "UserAggregate")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserAggregate {
    @AggregateIdentifier
    UUID userId;
    String fullName;
    Gender gender;
    String email;
    String phoneNumber;
    Long roleId;

    @CommandHandler
    public UserAggregate(CreateUserCommand createUserCommand){
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(createUserCommand.getUserId())
                .fullName(createUserCommand.getFullName())
                .gender(createUserCommand.getGender())
                .email(createUserCommand.getEmail())
                .password(createUserCommand.getPassword())
                .phoneNumber(createUserCommand.getPhoneNumber())
                .roleId(createUserCommand.getRoleId())
                .build();
        AggregateLifecycle.apply(userCreatedEvent);
    }


    @EventSourcingHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        this.userId = userCreatedEvent.getUserId();
        this.fullName = userCreatedEvent.getFullName();
        this.gender = userCreatedEvent.getGender();
        this.email = userCreatedEvent.getEmail();
        this.phoneNumber = userCreatedEvent.getPhoneNumber();
        this.roleId = userCreatedEvent.getRoleId();
    }
}
