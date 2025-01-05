package com.ttdat.authservice.application.commands;

import com.ttdat.authservice.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserCommand {
    @TargetAggregateIdentifier
    UUID userId;
    String fullName;
    Gender gender;
    String email;
    String password;
    String phoneNumber;
    Long roleId;
}
