package com.ttdat.purchaseservice.application.commands.purchaseorder;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CancelPurchaseOrderCommand {
    @TargetAggregateIdentifier
    String purchaseOrderId;

    String cancelReason;
}
